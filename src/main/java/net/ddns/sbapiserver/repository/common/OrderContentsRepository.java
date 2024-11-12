package net.ddns.sbapiserver.repository.common;

import net.ddns.sbapiserver.domain.entity.order.OrderContents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderContentsRepository extends JpaRepository<OrderContents, Integer> {
    List<OrderContents> findOrderContentsByOrdersOrderId(int orderId);

}
