package net.ddns.sbapiserver.domain.dto.basket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import net.ddns.sbapiserver.domain.dto.common.ManufacturerDto;
import net.ddns.sbapiserver.domain.dto.common.ManufacturerSortDto;
import net.ddns.sbapiserver.domain.entity.basket.StarBasket;

import java.util.List;
import java.util.stream.Collectors;

public interface StarBasketDto {

    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "starBasketCreate")
    @Data
    class Create{
        @Schema(name = "client_id")
        private int clientId;

        @Schema(name = "product_id")
        private int productId;

    }

    @Schema(name = "StarBasketResult")
    @Data
    @Builder
    class Result{
        @Schema(name = "start_basket_id")
        private int starBasketId;

        @Schema(name = "client_id")
        private int clientId;

        @Schema(name = "product_id")
        private int productId;

        @Schema(name = "product_name")
        private String productName;

        @Schema(name = "product_status")
        private String productStatus;

        @Schema(name = "product_img")
        private String productImg;

        @Schema(name = "manufacturer_sort_name")
        private String manufacturerSortName;

        @Schema(name = "manufacturer_name")
        private String manufacturerName;


        public static Result of(StarBasket starBasket){
            return Result.builder()
                    .starBasketId(starBasket.getStarBasketId())
                    .clientId(starBasket.getClients().getClientId())
                    .productId(starBasket.getProducts().getProductId())
                    .productName(starBasket.getProducts().getProductName())
                    .productStatus(starBasket.getProducts().getProductStatus())
                    .productImg(starBasket.getProducts().getProductImg())
                    .manufacturerSortName(starBasket.getProducts().getManufacturerSort().getSortName())
                    .manufacturerName(starBasket.getProducts().getManufacturers().getManufacturerName())
                    .build();
        }

        public static List<Result> of(List<StarBasket> starBasketList){
            return starBasketList.stream().map(Result::of)
                    .collect(Collectors.toList());
        }
    }
}
