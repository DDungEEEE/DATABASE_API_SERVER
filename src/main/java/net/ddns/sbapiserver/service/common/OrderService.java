package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.EditOrderStatusDto;
import net.ddns.sbapiserver.domain.dto.order.OrderContentDto;
import net.ddns.sbapiserver.domain.dto.order.OrderDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.order.OrderContents;
import net.ddns.sbapiserver.domain.entity.order.OrderStatus;
import net.ddns.sbapiserver.domain.entity.order.Orders;
import net.ddns.sbapiserver.repository.common.*;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderContentsRepository orderContentsRepository;
    private final CustomOrderRepository customOrderRepository;
    private final ServiceErrorHelper serviceErrorHelper;
    private final TaskScheduler taskScheduler;

    @Transactional
    public OrderDto.Result saveOrder(OrderDto.Create orderCreate){

        Clients clients = serviceErrorHelper.findClientsOrElseThrow404(orderCreate.getClientId());

        Orders order = orderCreate.asEntity(
                orders -> orders.withClients(clients)
        );

        System.out.println("시작 시간 : " + LocalDateTime.now());

        Orders saveOrder = orderRepository.save(order);

        List<OrderContentDto.Create> orderItemCreateList = orderCreate.getOrderItemCreateList();
        List<OrderContentDto.Result> orderContents = saveOrderContent(orderItemCreateList, order);

        scheduledOrderStatusUpdate(saveOrder);
        return OrderDto.Result.of(saveOrder, orderContents);
    }

    @Transactional(readOnly = true)
    public OrderDto.Result findOrder(int orderId){
        Orders findOrder = serviceErrorHelper.findOrderOrElseThrow404(orderId);
        return parsingToOneOrderDtoResult(findOrder);
    }

    protected void scheduledOrderStatusUpdate(Orders orders){
        LocalTime now = LocalTime.now();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        if(now.isAfter(startTime) && now.isBefore(endTime)){
            taskScheduler.schedule(() -> {
                orderRepository.updateOrderStatus(orders.getOrderId(), "출고 준비중");
                System.out.println("출고 준비 중 시간 : " + LocalDateTime.now());

                taskScheduler.schedule(() -> {
                    orderRepository.updateOrderStatus(orders.getOrderId(), "배송중");
                    System.out.println("배송 중 시간 : " + LocalDateTime.now());
                }, Instant.now().plusSeconds(30 * 60));
            }, Instant.now().plusSeconds(30 * 60));
        }
    }

    @Transactional
    protected List<OrderContentDto.Result> saveOrderContent(List<OrderContentDto.Create> orderCreates, Orders orders){
        List<OrderContents> collectOrderContents = orderCreates.stream()
                .map(orderCreate -> orderCreate.asEntity(orderContents ->
                        orderContents
                                .withOrders(orders)
                                .withProducts(serviceErrorHelper.findProductsOrElseThrow404(orderCreate.getProductId())))
                ).collect(Collectors.toList());

        List<OrderContents> orderContents = orderContentsRepository.saveAll(collectOrderContents);

        return OrderContentDto.Result.of(orderContents);
    }

    @Transactional(readOnly = true)
    public List<OrderDto.Result> getOrderResultList(int clientId, LocalDate startDate, LocalDate endDate){
        serviceErrorHelper.findClientsOrElseThrow404(clientId);

        List<Orders> ordersList = customOrderRepository.findOrder(clientId, startDate, endDate);
        List<OrderDto.Result> resultOrderResult = parsingToOrdersDtoResult(ordersList);
        resultOrderResult.sort(Comparator.comparing(OrderDto.Result::getOrderDate));
        Collections.reverse(resultOrderResult);

        return resultOrderResult;
    }

    @Transactional
    public void deleteOrder(int orderId){
        serviceErrorHelper.findOrderOrElseThrow404(orderId);
        customOrderRepository.deleteOrderContent(orderId);
    }

    @Transactional(readOnly = true)
    public List<OrderDto.Result> getAllOrderList(LocalDate startDate, LocalDate endDate){
        List<Orders> orderListForAdmin = customOrderRepository.getOrderListForAdmin(startDate, endDate);
        List<OrderDto.Result> adminResultOrders = parsingToOrdersDtoResult(orderListForAdmin);

        adminResultOrders.sort(Comparator.comparing(OrderDto.Result::getOrderDate));
        Collections.reverse(adminResultOrders);

        return adminResultOrders;

    }

    @Transactional
    public void setOrderPrintCheck(int orderId){
        Orders findOrder = serviceErrorHelper.findOrderOrElseThrow404(orderId);
        findOrder.setOrderPrintCk(1);
        orderRepository.save(findOrder);
    }

    @Transactional
    public OrderDto.Result updateOrderStatus(EditOrderStatusDto editOrderStatusDto){
        Orders findOrder = serviceErrorHelper.findOrderOrElseThrow404(editOrderStatusDto.getOrderId());
        findOrder.setOrderStatus(editOrderStatusDto.getOrderStatus());
        Orders saveOrder = orderRepository.save(findOrder);

        return parsingToOneOrderDtoResult(saveOrder);
    }

    // 결과 Order List를 ResultDto 로 바꾸는 method
    protected List<OrderDto.Result> parsingToOrdersDtoResult(List<Orders> ordersList){
        return ordersList.stream().map(
                orders -> {
                    List<OrderContents> orderContent = customOrderRepository.findOrderContent(orders.getOrderId());
                    return OrderDto.Result.of(orders, OrderContentDto.Result.of(orderContent));
                }).collect(Collectors.toList());
    }

    protected OrderDto.Result parsingToOneOrderDtoResult(Orders orders){
        List<OrderContents> orderContent = customOrderRepository.findOrderContent(orders.getOrderId());
        return OrderDto.Result.of(orders, OrderContentDto.Result.of(orderContent));
    }
}
