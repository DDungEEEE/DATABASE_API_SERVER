package net.ddns.sbapiserver.domain.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.order.OrderContents;
import net.ddns.sbapiserver.domain.entity.order.Orders;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface OrderContentDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "OrderContentCreate")
    @Builder
    class Create{

        @Schema(name = "product_id")
        private int productId;
        @Schema(name = "product_cnt")
        private int productCnt;

        public OrderContents asEntity(Function<? super OrderContents, ? extends OrderContents> init){
            return init.apply(
                            OrderContents.builder()
                            .productCnt(productCnt)
                            .build()
            );
        }

    }

    @Schema(name = "OrderContentResult")
    @Data
    @Builder
    class Result{
        @Schema(name = "order_content_id")
        private int orderContentId;

        @Schema(name = "product_name")
        private String productName;

        @Schema(name = "product_cnt")
        private int productCnt;

        @Schema(name = "product_id")
        private int productId;

        @Schema(name = "manufacturer_name")
        private String manufacturerName;

        @Schema(name = "sort_name")
        private String sortName;

        /**
         * ClientId로 Orders를 조회할 때의 OrderContent 데이터
         */
        public static Result of(OrderContents orderContents){
            return Result.builder()
                    .orderContentId(orderContents.getOrderContentId())
                    .productName(orderContents.getProducts().getProductName())
                    .productId(orderContents.getProducts().getProductId())
                    .productCnt(orderContents.getProductCnt())
                    .manufacturerName(orderContents.getProducts().getManufacturers().getManufacturerName())
                    .sortName(orderContents.getProducts().getManufacturerSort().getSortName())
                    .build();
        }

        public static List<Result> of(List<OrderContents> orderContents){
            return orderContents.stream()
                    .map(Result::of)
                    .collect(Collectors.toList());
        }
    }
}
