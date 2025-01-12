package net.ddns.sbapiserver.domain.dto.basket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import net.ddns.sbapiserver.domain.dto.common.ManufacturerDto;
import net.ddns.sbapiserver.domain.dto.common.ManufacturerSortDto;
import net.ddns.sbapiserver.domain.entity.basket.MarketBasket;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface MarketBasketDto {

    @Data
    @Schema(name = "MarketBasketCreate")
    @AllArgsConstructor
    @NoArgsConstructor
    class Create{

        @Schema(name = "client_id")
        private int clientId;

        @Schema(name = "market_basket_items")
        private List<MarketBasketItem> marketBasketItems;

        @Data
        @Schema(name = "MarketBasketItem")
        public static class MarketBasketItem{
            @Schema(name = "product_id")
            private int productId;

            @Schema(name = "product_cnt")
            private int productCnt;

        }
    }

    @Data
    @Schema(name = "MarketBasketResult")
    @Builder
    class Result{

        @Schema(name = "market_basket_id")
        private int marketBasketId;

        @Schema(name = "product_name")
        private String productName;

        @Schema(name = "product_cnt")
        private int productCnt;

        @Schema(name = "product_id")
        private int productId;

        @Schema(name = "manufacturer_sort_name")
        private String manufacturerSortName;
        @Schema(name = "manufacturer_name")
        private String manufacturerName;

        public static Result of(MarketBasket marketBasket){
            return Result.builder()
                    .marketBasketId(marketBasket.getMarketBasketId())
                    .productName(marketBasket.getProducts().getProductName())
                    .productCnt(marketBasket.getProductCnt())
                    .productId(marketBasket.getProducts().getProductId())
                    .manufacturerName(marketBasket.getProducts().getManufacturers().getManufacturerName())
                    .manufacturerSortName(marketBasket.getProducts().getManufacturerSort().getSortName())
                    .build();

        }

        public static List<Result> of(List<MarketBasket> marketBasketsList){
            Stream<MarketBasket> marketBasketList = marketBasketsList.stream();
            return marketBasketList
                    .map(Result::of)
                    .collect(Collectors.toList());
        }
    }


}
