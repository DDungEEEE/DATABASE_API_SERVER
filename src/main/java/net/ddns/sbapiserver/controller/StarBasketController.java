package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.basket.StarBasketDto;
import net.ddns.sbapiserver.service.basket.StarBasketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "즐겨찾기 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/api/starbasket")
@RestController
public class StarBasketController {
    private final StarBasketService starBasketService;

    @ApiResponse(responseCode = "200", description = "즐겨찾기 취소 성공")
    @ApiResponse(responseCode = "201", description = "즐겨찾기 추가 성공")
    @Operation(summary = "즐겨찾기 추가, 취소", description = "즐겨찾기를 추가하고, 만약 이미 등록된 즐겨찾기일 경우 client_id, product_id 기준으로 취소합니다.")
    @PostMapping
    public ResultResponse<StarBasketDto.Result> createStarBasket(@RequestBody StarBasketDto.Create create){
        StarBasketDto.Result startBasket = starBasketService.createStartBasket(create);
        if(startBasket==null){
            return ResultResponse.<StarBasketDto.Result>successResponse()
                    .successCode(SuccessCode.DELETE_SUCCESS)
                    .build();
        }else {
            return ResultResponse.<StarBasketDto.Result>successResponse()
                    .result(startBasket)
                    .successCode(SuccessCode.INSERT_SUCCESS)
                    .build();
        }
    }

    @ApiResponse(responseCode = "200", description = "즐겨찾기 취소")
    @Operation(summary = "즐겨찾기 취소")
    @DeleteMapping("/{star_basket_id}")
    public ResultResponse<Void> deleteStarBasket(@PathVariable("star_basket_id") int starBasketId){
        starBasketService.deleteStarBasket(starBasketId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }

    @ApiResponse(responseCode = "200", description = "즐겨찾기 목록 조회")
    @GetMapping("/{client_id}")
    public ResultResponse<List<StarBasketDto.Result>> getStarBasketList(@PathVariable("client_id") int clientId){
        List<StarBasketDto.Result> starBasketList = starBasketService.getStarBasketList(clientId);
        return ResultResponse.<List<StarBasketDto.Result>>successResponse()
                .result(starBasketList)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

}
