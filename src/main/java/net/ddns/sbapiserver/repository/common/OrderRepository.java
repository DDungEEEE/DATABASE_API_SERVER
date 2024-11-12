package net.ddns.sbapiserver.repository.common;

import net.ddns.sbapiserver.domain.entity.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Integer> {
    List<Orders> findOrdersByClientsClientId(int clientId);
}
