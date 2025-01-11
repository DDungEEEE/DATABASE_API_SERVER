package net.ddns.sbapiserver.domain.dto.basket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import net.ddns.sbapiserver.domain.entity.basket.StarBasket;

import java.util.List;
import java.util.stream.Collectors;

public interface StarBasketDto {

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

        private String productName;


        public static Result of(StarBasket starBasket){
            return Result.builder()
                    .starBasketId(starBasket.getStarBasketId())
                    .clientId(starBasket.getClients().getClientId())
                    .productId(starBasket.getProducts().getProductId())
                    .build();
        }

        public static List<Result> of(List<StarBasket> starBasketList){
            return starBasketList.stream().map(Result::of)
                    .collect(Collectors.toList());
        }
    }
}
