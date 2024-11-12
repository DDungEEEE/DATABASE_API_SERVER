package net.ddns.sbapiserver.controller;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.order.OrderDto;
import net.ddns.sbapiserver.service.order.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequestMapping("/api/client/order")
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResultResponse<OrderDto.Result> createOrder(@RequestBody OrderDto.Create orderCreate){
        OrderDto.Result orderResult = orderService.saveOrder(orderCreate);
        return ResultResponse.<OrderDto.Result>dataResponse()
                .result(orderResult)
                .resultCode(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("{client_id}/{start_date}/{end_date}")
    public ResultResponse<List<OrderDto.Result>> getOrder(@PathVariable("client_id") int clientId, @PathVariable("start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){

        List<OrderDto.Result> orderData = orderService.getOrderData(clientId);

        return ResultResponse.<List<OrderDto.Result>>dataResponse()
                .result(orderData)
                .resultCode(HttpStatus.OK)
                .build();
    }
}
