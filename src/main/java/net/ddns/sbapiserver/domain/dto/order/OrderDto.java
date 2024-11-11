package net.ddns.sbapiserver.domain.dto.order;

import jakarta.persistence.criteria.Order;
import lombok.Builder;
import lombok.Data;
import net.ddns.sbapiserver.domain.entity.order.Orders;

import java.util.List;
import java.util.stream.Collectors;

public interface OrderDto {

    @Data
    @Builder
    class Create{
        private int clientId;

        private List<OrderItemCreate> orderItemCreateList;

        private String OrderRequest;

        @Data
        static class OrderItemCreate{
            private String productId;
            private int productCnt;
        }
    }

    @Builder
    @Data
    class Result{

        private int orderId;
        private String orderDate;
        private int orderPrintCk;
        private String orderRequest;
        private String orderStatus;
        private int clientId;

        public static Result of(Orders order){
            return Result.builder()
                    .orderId(order.getOrderId())
                    .orderDate(order.getOrderDate())
                    .orderPrintCk(order.getOrderPrintCk())
                    .orderRequest(order.getOrderRequest())
                    .orderStatus(order.getOrderStatus())
                    .clientId(order.getClients().getClientId())
                    .build();
        }

        public static List<Result> of(List<Orders> orders){
            return orders.stream().map(Result::of)
                    .collect(Collectors.toList());
        }
    }
}
