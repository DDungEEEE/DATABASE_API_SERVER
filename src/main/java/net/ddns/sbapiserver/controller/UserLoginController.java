package net.ddns.sbapiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.UserLoginDto;
import net.ddns.sbapiserver.service.login.LoginService;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인, 로그아웃 api")
@RequestMapping("/api/user")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserLoginController {
    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "사용자 로그인", description = "checkSum --> 강제 로그인 여부 1 : 강제 로그인 0 : 강제 로그인 X")
    @PostMapping("/login")
    public void login(@RequestBody @Parameter UserLoginDto userLoginDto, HttpServletRequest req)throws Exception{
    }

    @Operation(summary = "사용자 로그아웃")
    @PostMapping("/logout")
    public ResultResponse<Void> logout(HttpServletRequest req){
        String accessToken = jwtUtil.getJwtToken(req);
        loginService.logout(jwtUtil.getClaims(accessToken).getSubject());
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.LOGOUT_SUCCESS)
                .build();
    }

}
