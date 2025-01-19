package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.AlertDto;
import net.ddns.sbapiserver.service.alert.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "알림 저장 api")
@RequiredArgsConstructor
@RequestMapping( "/api/v1/alert")
@RestController
public class AlertController {
    private final AlertService alertService;

    @Operation(summary = "모든 알림들 조회")
    @GetMapping
    public ResultResponse<List<AlertDto.Result>> getAllAlerts(){
        return ResultResponse.<List<AlertDto.Result>>successResponse()
                .result(alertService.getAllAlertList())
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }
    @Operation(summary = "새 알림 등록")
    @PostMapping
    public ResultResponse<AlertDto.Result> createAlert(@RequestBody AlertDto.Create create){
        AlertDto.Result saveAlert = alertService.createAlert(create);
        return ResultResponse.<AlertDto.Result>successResponse()
                .result(saveAlert)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }
    @Operation(summary = "알림 수정")
    @PutMapping
    public ResultResponse<AlertDto.Result> updateAlert(@RequestBody AlertDto.Put put){

        return ResultResponse.<AlertDto.Result>successResponse()
                .result(alertService.updateAlert(put))
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }
    @Operation(summary = "알림 삭제")
    @DeleteMapping("/{alert_id}")
    public ResultResponse<Void> deleteAlert(@PathVariable(value = "alert_id") int alertId){
        alertService.deleteAlert(alertId);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}
