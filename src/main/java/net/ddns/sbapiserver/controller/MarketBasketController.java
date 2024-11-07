package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.basket.MarketBasketDto;
import net.ddns.sbapiserver.domain.entity.basket.MarketBasket;
import net.ddns.sbapiserver.service.basket.MarketBasketService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/client/marketbasket")
public class MarketBasketController {

    private final MarketBasketService marketBasketService;

    @Operation(summary = "장바구니 추가")
    @PostMapping
    public ResultResponse<List<MarketBasketDto.Result>> create(@RequestBody @Valid List<MarketBasketDto.Create> create){
        List<MarketBasketDto.Result> results = marketBasketService.saveMarketBasket(create);
        return ResultResponse.<List<MarketBasketDto.Result>>dataResponse()
                .result(results)
                .resultCode(HttpStatus.CREATED)
                .build();
    }
}
