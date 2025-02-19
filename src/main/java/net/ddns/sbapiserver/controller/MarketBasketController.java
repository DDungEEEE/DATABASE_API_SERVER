package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.basket.MarketBasketDto;
import net.ddns.sbapiserver.domain.entity.basket.MarketBasket;
import net.ddns.sbapiserver.service.authentication.AuthenticationService;
import net.ddns.sbapiserver.service.basket.MarketBasketService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "장바구니 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/marketbasket")
public class MarketBasketController {

    private final MarketBasketService marketBasketService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @ApiResponse(responseCode = "200")
    @Operation(summary = "장바구니 추가")
    @PostMapping
    public ResultResponse<List<MarketBasketDto.Result>> createMarketBasket(@RequestBody @Valid MarketBasketDto.Create create, Authentication authentication){
        authenticationService.isOwner(authentication, create.getClientId());

        List<MarketBasketDto.Result> results = marketBasketService.saveMarketBasket(create);
        return ResultResponse.<List<MarketBasketDto.Result>>successResponse()
                .result(results)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @Operation(summary = "장바구니 불러오기")
    @ApiResponse(responseCode = "200")
    @GetMapping("{client_id}")
    public ResultResponse<List<MarketBasketDto.Result>> getMarketBaskets(@PathVariable("client_id") int clientId, Authentication authentication){
        authenticationService.isOwner(authentication, clientId);

        marketBasketService.getMarketBasketList(clientId);
        return ResultResponse.<List<MarketBasketDto.Result>>successResponse()
                .result(marketBasketService.getMarketBasketList(clientId))
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @Operation(summary = "장바구니 단일건 수정", description = "장바구니 상품 개수 수정")
    @PutMapping
    public ResultResponse<MarketBasketDto.Result> updateMarketBasket(@RequestBody MarketBasketDto.Put put){
        MarketBasketDto.Result result = marketBasketService.editMarketBasket(put);
        return ResultResponse.<MarketBasketDto.Result>successResponse()
                .result(result)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @Operation(summary = "장바구니 단일건 삭제")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("{market_basket_id}")
    public ResultResponse<Void> deleteOne(@PathVariable("market_basket_id") int marketBasketId){
        marketBasketService.deleteByMarketBasketId(marketBasketId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }

    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_CLIENT')")
    @Operation(summary = "장바구니 전체 삭제")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/all/{client_id}")
    public ResultResponse<Void> deleteAll(@PathVariable("client_id") int clientId){
        marketBasketService.deleteByClientId(clientId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}
