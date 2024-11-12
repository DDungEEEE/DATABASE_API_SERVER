package net.ddns.sbapiserver.repository.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.order.OrderContentDto;
import net.ddns.sbapiserver.domain.dto.order.OrderDto;
import net.ddns.sbapiserver.domain.entity.order.QOrders;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomOrderRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<OrderDto.Result> findOrder(int clientId, LocalDate startDate, LocalDate endDate){
        QOrders orders = QOrders.orders;

        jpaQueryFactory.select(orders)
                .where(orders.clients.clientId.eq(clientId))
                .where(orders.orderDate.between(startDate, endDate))
    }

    protected List<OrderContentDto.Result> findOrderContent(int orderId){

    }
}
