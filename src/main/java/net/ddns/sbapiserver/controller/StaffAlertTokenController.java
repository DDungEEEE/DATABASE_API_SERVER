package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.alert.StaffAlertTokenDto;
import net.ddns.sbapiserver.service.alert.StaffAlertTokenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "직원 알림 컨트롤러")
@RequestMapping("/api/v1/staff-alert-token")
@RestController
@RequiredArgsConstructor
public class StaffAlertTokenController {
    private final StaffAlertTokenService staffAlertTokenService;

    @GetMapping("/{staff_id}")
    public ResultResponse<List<StaffAlertTokenDto.Result>> getStaffToken(@PathVariable("staff_id") int staffId){
        List<StaffAlertTokenDto.Result> staffAlertTokens = staffAlertTokenService.findStaffAlertTokensByStaffId(staffId);
        return ResultResponse.<List<StaffAlertTokenDto.Result>>successResponse()
                .result(staffAlertTokens)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @Operation(summary = "모든 직원 알림토큰 조회")
    @GetMapping
    public ResultResponse<List<StaffAlertTokenDto.Result>> getAllStaffTokens(){
        List<StaffAlertTokenDto.Result> allStaffAlertTokens = staffAlertTokenService.getAllStaffAlertTokens();
        return ResultResponse.<List<StaffAlertTokenDto.Result>>successResponse()
                .result(allStaffAlertTokens)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @PostMapping
    public ResultResponse<StaffAlertTokenDto.Result> createStaffToken(@RequestBody StaffAlertTokenDto.Create create){
        StaffAlertTokenDto.Result createStaffAlertToken = staffAlertTokenService.createStaffAlertToken(create);
        return ResultResponse.<StaffAlertTokenDto.Result>successResponse()
                .result(createStaffAlertToken)
                .successCode(SuccessCode.INSERT_SUCCESS)
                .build();
    }

    @DeleteMapping
    public ResultResponse<Void> deleteStaffToken(@RequestBody StaffAlertTokenDto.Delete delete){
        staffAlertTokenService.deleteStaffAlertToken(delete);
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}

