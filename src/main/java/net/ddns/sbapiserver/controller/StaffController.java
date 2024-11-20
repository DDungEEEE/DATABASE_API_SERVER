package net.ddns.sbapiserver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.staff.StaffDto;
import net.ddns.sbapiserver.service.StaffService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    ResultResponse<StaffDto.Result> add(@RequestBody @Valid StaffDto.Create create){
        StaffDto.Result staff = staffService.createStaff(create);
        return ResultResponse.<StaffDto.Result>successResponse()
                .successCode(SuccessCode.INSERT_SUCCESS)
                .result(staff)
                .build();
    }
}
