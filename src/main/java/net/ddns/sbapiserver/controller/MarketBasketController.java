package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.basket.MarketBasketDto;
import net.ddns.sbapiserver.domain.entity.basket.MarketBasket;
import net.ddns.sbapiserver.service.basket.MarketBasketService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/marketbasket")
public class MarketBasketController {

    private final MarketBasketService marketBasketService;

    @Operation(summary = "장바구니 추가")
    @PostMapping
    public ResultResponse<List<MarketBasketDto.Result>> create(@RequestBody @Valid List<MarketBasketDto.Create> create){
        List<MarketBasketDto.Result> results = marketBasketService.saveMarketBasket(create);
        return ResultResponse.<List<MarketBasketDto.Result>>successResponse()
                .result(results)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }

    @Operation(summary = "장바구니 불러오기")
    @ApiResponse(responseCode = "200")
    @GetMapping("{client_id}")
    public ResultResponse<List<MarketBasketDto.Result>> getMarketBaskets(@PathVariable("client_id") int clientId){
        marketBasketService.getMarketBasketList(clientId);
        return ResultResponse.<List<MarketBasketDto.Result>>successResponse()
                .result(marketBasketService.getMarketBasketList(clientId))
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @Operation(summary = "장바구니 삭제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("{client_id}")
    public ResultResponse<Void> delete(@PathVariable("client_id") int clientId){
        marketBasketService.deleteByClientId(clientId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}
