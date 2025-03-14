package net.ddns.sbapiserver.repository.common;

import net.ddns.sbapiserver.domain.entity.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Integer> {
    List<Orders> findOrdersByClientsClientId(int clientId);

    @Modifying
    @Transactional
    @Query("UPDATE Orders o SET o.orderStatus = :orderStatus WHERE o.orderId = :orderId")
    void updateOrderStatus(Integer orderId, String orderStatus);
}
