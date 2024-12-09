package net.ddns.sbapiserver.controller.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.order.OrderDto;
import net.ddns.sbapiserver.service.common.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "주문 컨트롤러")
@RequestMapping("/api/order")
@RestController
@RequiredArgsConstructor
public class TestOrderController {
    private final OrderService orderService;

    @ApiResponse(responseCode = "201", description = "주문 추가")
    @Operation(summary = "주문 추가")
    @PostMapping
    public ResultResponse<OrderDto.Result> createOrder(@RequestBody OrderDto.Create orderCreate){
        OrderDto.Result orderResult = orderService.saveOrder(orderCreate);
        return ResultResponse.<OrderDto.Result>successResponse()
                .result(orderResult)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }
    @ApiResponse(responseCode = "200")
    @Operation(summary = "관리자가 사용할 주문내역 조회")
    @GetMapping("{start_date}/{end_date}")
    public ResultResponse<List<OrderDto.Result>> getAllOrder(@PathVariable("start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate startDate, @PathVariable("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){

        List<OrderDto.Result> orderResultList = orderService.getAllOrderList(startDate, endDate);

        return ResultResponse.<List<OrderDto.Result>>successResponse()
                .result(orderResultList)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }


    @ApiResponse(responseCode = "200")
    @Operation(summary = "주문내역 조회")
    @GetMapping("{client_id}/{start_date}/{end_date}")
    public ResultResponse<List<OrderDto.Result>> getOrder(@PathVariable("client_id") int clientId, @PathVariable("start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate startDate, @PathVariable("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){

        List<OrderDto.Result> orderResultList = orderService.getOrderResultList(clientId, startDate, endDate);

        return ResultResponse.<List<OrderDto.Result>>successResponse()
                .result(orderResultList)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @Operation(summary = "주문내역 삭제")
    @DeleteMapping("{order_id}")
    public ResultResponse<Void> deleteOrder( @PathVariable("order_id") int orderId){
        orderService.deleteOrder(orderId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}
