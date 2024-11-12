package net.ddns.sbapiserver.service.order;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.order.OrderContentDto;
import net.ddns.sbapiserver.domain.dto.order.OrderDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.domain.entity.order.OrderContents;
import net.ddns.sbapiserver.domain.entity.order.Orders;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.common.ManufacturersRepository;
import net.ddns.sbapiserver.repository.common.OrderContentsRepository;
import net.ddns.sbapiserver.repository.common.OrderRepository;
import net.ddns.sbapiserver.repository.common.ProductsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderContentsRepository orderContentsRepository;
    private final ClientRepository clientRepository;
    private final ManufacturersRepository manufacturersRepository;
    private final ProductsRepository productsRepository;

    @Transactional
    public OrderDto.Result saveOrder(OrderDto.Create orderCreate){

        Clients clients = clientRepository.findById(orderCreate.getClientId()).get();

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
                                .withProducts(productsRepository.findByProductId(orderCreate.getProductId())))
                ).collect(Collectors.toList());

        List<OrderContents> orderContents = orderContentsRepository.saveAll(collectOrderContents);

        return OrderContentDto.Result.of(orderContents);
    }

    public List<OrderDto.Result> getOrderData(int clientId){
        List<Orders> orders = orderRepository.findOrdersByClientsClientId(clientId);

        List<OrderDto.Result> orderResponseResult = new ArrayList<>();

        for(int i = 0; i < orders.size(); i++){
            int orderId = orders.get(i).getOrderId();

            List<OrderContents> orderContents = orderContentsRepository.findOrderContentsByOrdersOrderId(orderId);

            List<OrderContentDto.Result> orderContentResult = OrderContentDto.Result.of(orderContents);

            OrderDto.Result orderResult = OrderDto.Result.of(orders.get(i), orderContentResult);

            orderResponseResult.add(orderResult);
        }

        return orderResponseResult;
    }
}
