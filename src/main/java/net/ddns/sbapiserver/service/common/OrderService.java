package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.order.OrderContentDto;
import net.ddns.sbapiserver.domain.dto.order.OrderDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.domain.entity.order.OrderContents;
import net.ddns.sbapiserver.domain.entity.order.Orders;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.common.*;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderContentsRepository orderContentsRepository;
    private final CustomOrderRepository customOrderRepository;
    private final ServiceErrorHelper serviceErrorHelper;

    @Transactional
    public OrderDto.Result saveOrder(OrderDto.Create orderCreate){

        Clients clients = serviceErrorHelper.findClientsOrElseThrow404(orderCreate.getClientId());

        Orders order = orderCreate.asEntity(
                orders -> orders.withClients(clients)
        );

        Orders saveOrder = orderRepository.save(order);

        List<OrderContentDto.Create> orderItemCreateList = orderCreate.getOrderItemCreateList();

        List<OrderContentDto.Result> orderContents = saveOrderContent(orderItemCreateList, order);

        return OrderDto.Result.of(saveOrder, orderContents);
    }

    @Transactional
    protected List<OrderContentDto.Result> saveOrderContent(List<OrderContentDto.Create> orderCreates, Orders orders){
        List<OrderContents> collectOrderContents = orderCreates.stream()
                .map(orderCreate -> orderCreate.asEntity(orderContents ->
                        orderContents
                                .withOrders(orders)
                                .withProducts(serviceErrorHelper.findProductsOrElseThrow404(orderContents.getProducts().getProductId())))
                ).collect(Collectors.toList());

        List<OrderContents> orderContents = orderContentsRepository.saveAll(collectOrderContents);

        return OrderContentDto.Result.of(orderContents);
    }

    @Transactional(readOnly = true)
    public List<OrderDto.Result> getOrderResultList(int clientId, LocalDate startDate, LocalDate endDate){
        serviceErrorHelper.findClientsOrElseThrow404(clientId);

        List<Orders> ordersList = customOrderRepository.findOrder(clientId, startDate, endDate);
       return parsingToOrderDtoResult(ordersList);
    }

    @Transactional
    public void deleteOrder(int orderId){
        serviceErrorHelper.findOrderOrElseThrow404(orderId);
        customOrderRepository.deleteOrderContent(orderId);
    }

    @Transactional(readOnly = true)
    public List<OrderDto.Result> getAllOrderList(LocalDate startDate, LocalDate endDate){
        List<Orders> orderListForAdmin = customOrderRepository.getOrderListForAdmin(startDate, endDate);
        return parsingToOrderDtoResult(orderListForAdmin);

    }

    protected List<OrderDto.Result> parsingToOrderDtoResult(List<Orders> ordersList){
        return ordersList.stream().map(
                orders -> {
                    List<OrderContents> orderContent = customOrderRepository.findOrderContent(orders.getOrderId());
                    List<OrderContentDto.Result> orderContentResults = OrderContentDto.Result.of(orderContent);
                    return OrderDto.Result.of(orders, orderContentResults);
                }).collect(Collectors.toList());
    }
}
