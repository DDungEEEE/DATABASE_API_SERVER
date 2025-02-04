package net.ddns.sbapiserver.domain.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.order.OrderContents;
import net.ddns.sbapiserver.domain.entity.order.Orders;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface OrderDto {

    /**
     * Orders테이블에 데이터 입력(orderRequest) orderDate, orderPrintCk, orderStatus는 자동입력
     * ->OrderContents 테이블 추가 입력(orderId, productId, productCnt)
     */
    @Schema(name = "OrderCreateDto")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class Create{
        @Schema(name = "client_id")
        private int clientId;

        @Schema(name = "order_item_create_list")
        private List<OrderContentDto.Create> orderItemCreateList;

        @Schema(name = "order_request")
        private String orderRequest;


        public Orders asEntity(
                Function<? super Orders, ? extends Orders > init){
               return init.apply(
                       Orders.builder()
                               .orderRequest(orderRequest)
                               .build());
        }

    }

    @Schema(name = "OrderResult")
    @Builder
    @Data
    class Result{

        @Schema(name = "order_id")
        private int orderId;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(name = "order_date")
        private LocalDateTime orderDate;

        @Schema(name = "order_print_ck")
        private int orderPrintCk;

        @Schema(name ="order_request")
        private String orderRequest;

        @Schema(name = "order_status")
        private String orderStatus;

        @Schema(name = "client_name")
        private String clientName;

        @Schema(name = "client_ceo_name")
        private String clientCeoName;

        @Schema(name = "client_addr")
        private String clientAddr;

        @Schema(name = "client_lag")
        private String clientLag;

        @Schema(name = "client_long")
        private String clientLong;

        @Schema(name = "order_contents")
        private List<OrderContentDto.Result> orderContents;

        public static Result of(Orders order, List<OrderContentDto.Result> orderContents){
            return Result.builder()
                    .clientName(order.getClients().getClientName())
                    .clientCeoName(order.getClients().getClientCeoName())
                    .orderId(order.getOrderId())
                    .orderDate(order.getOrderDate())
                    .orderPrintCk(order.getOrderPrintCk())
                    .orderRequest(order.getOrderRequest())
                    .orderStatus(order.getOrderStatus())
                    .orderContents(orderContents)
                    .clientLag(order.getClients().getClientLag())
                    .clientLong(order.getClients().getClientLong())
                    .clientAddr(order.getClients().getClientAddr()
                    )
                    .build();
        }

    }
}
