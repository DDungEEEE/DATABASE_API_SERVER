package net.ddns.sbapiserver.repository.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.dto.order.OrderContentDto;
import net.ddns.sbapiserver.domain.dto.order.OrderDto;
import net.ddns.sbapiserver.domain.entity.order.OrderContents;
import net.ddns.sbapiserver.domain.entity.order.Orders;
import net.ddns.sbapiserver.domain.entity.order.QOrderContents;
import net.ddns.sbapiserver.domain.entity.order.QOrders;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class CustomOrderRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public List<Orders> findOrder(int clientId, LocalDate startDate, LocalDate endDate){
        QOrders orders = QOrders.orders;

        LocalDateTime startLocalDate = parsingToStartLocalDateTime(startDate);
        LocalDateTime endLocalDate = parsingToEndLocalDateTime(endDate);


        List<Orders> orderList = jpaQueryFactory.selectFrom(orders)
                .where(orders.clients.clientId.eq(clientId)
                        .and(orders.orderDate.between(startLocalDate, endLocalDate)))
                .fetch();


        return orderList;
    }

    @Transactional
    public List<OrderContents> findOrderContent(int orderId){
        QOrderContents qOrderContents = QOrderContents.orderContents;

        List<OrderContents> orderContentsList = jpaQueryFactory.select(qOrderContents)
                .from(qOrderContents)
                .where(qOrderContents.orders.orderId.eq(orderId))
                .fetch();

        return orderContentsList;
    }

    protected LocalDateTime parsingToStartLocalDateTime(LocalDate localDate){
        return localDate.atStartOfDay();
    }

    protected LocalDateTime parsingToEndLocalDateTime(LocalDate localDate){
        return localDate.atTime(LocalTime.MAX);
    }

    @Transactional
    public void deleteOrderContent(int orderId){
        QOrders qOrders = QOrders.orders;
        QOrderContents qOrderContents = QOrderContents.orderContents;

        jpaQueryFactory.delete(qOrderContents)
                .where(qOrderContents.orders.orderId.eq(orderId))
                .execute();

        jpaQueryFactory.delete(qOrders)
                .where(qOrders.orderId.eq(orderId))
                .execute();

    }

}
