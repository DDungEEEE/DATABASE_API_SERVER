package net.ddns.sbapiserver.domain.dto.basket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ddns.sbapiserver.domain.entity.basket.MarketBasket;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface MarketBasketDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Create{

        @Schema(name = "client_id")
        private int clientId;

        @Schema(name = "product_id")
        private int productId;

        @Schema(name = "product_cnt")
        private int productCnt;

        public MarketBasket asEntity(
                Function<? super MarketBasket, ? extends MarketBasket> init){
            return init.apply(
                    MarketBasket.builder()
                            .productCnt(productCnt)
                            .build());
        }
    }

    @Data
    @Builder
    class Result{

        @Schema(name = "market_bakset_id")
        private int marketBasketId;

        @Schema(name = "product_name")
        private String productName;

        @Schema(name = "product_cnt")
        private int productCnt;

        @Schema(name = "product_id")
        private int productId;

        public static Result of(MarketBasket marketBasket){
            return Result.builder()
                    .marketBasketId(marketBasket.getMarketBasketId())
                    .productName(marketBasket.getProducts().getProductName())
                    .productCnt(marketBasket.getProductCnt())
                    .productId(marketBasket.getProducts().getProductId())
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
