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

        Timestamp startTimestamp = parsingToTimestamp(startDate);
        Timestamp endTimestamp = parsingToTimestamp(endDate);

        System.out.println("endTimestamp = " + endTimestamp + "adas = :" + startTimestamp);

        List<Orders> orderList = jpaQueryFactory.select(orders)
                .where(orders.clients.clientId.eq(clientId))
                .where(orders.orderDate.between(startTimestamp, endTimestamp))
                .fetch();


        return orderList;
    }

    @Transactional
    public List<OrderContents> findOrderContent(int orderId){
        QOrderContents qOrderContents = QOrderContents.orderContents;

        List<OrderContents> orderContentsList = jpaQueryFactory.select(qOrderContents)
                .where(qOrderContents.orders.orderId.eq(orderId))
                .fetch();

        return orderContentsList;
    }

    private Timestamp parsingToTimestamp(LocalDate localDate){
        LocalDateTime localDateTime = localDate.atStartOfDay();
        return Timestamp.valueOf(localDateTime);
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
