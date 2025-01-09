package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.staff.StaffDto;
import net.ddns.sbapiserver.service.common.StaffService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "직원 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final StaffService staffService;

    @Operation(summary = "관리자 회원가입")
    @ApiResponse(responseCode = "200")
    @PostMapping
    ResultResponse<StaffDto.Result> addStaff(@RequestBody @Valid StaffDto.Create create){

        StaffDto.Result staff = staffService.createStaff(create);
        return ResultResponse.<StaffDto.Result>successResponse()
                .successCode(SuccessCode.INSERT_SUCCESS)
                .result(staff)
                .build();
    }

    @Operation(summary = "관리자 회원정보 수정")
    @ApiResponse(responseCode = "200")
    @PutMapping
    ResultResponse<StaffDto.Result> updateStaff(@RequestBody StaffDto.Put put){
        StaffDto.Result result = staffService.updateStaff(put);
        return ResultResponse.<StaffDto.Result>successResponse()
                .result(result)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }

    @Operation(summary = "관리자 회원탈퇴")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("{staff_id}")
    ResultResponse<Void> deleteStaff(@PathVariable("staff_id") int staffId){
        staffService.deleteStaff(staffId);

        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.DELETE_SUCCESS)
                .build();
    }
}
