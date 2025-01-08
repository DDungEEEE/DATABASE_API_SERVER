package net.ddns.sbapiserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.UserLoginDto;
import net.ddns.sbapiserver.service.common.UserLoginService;
import net.ddns.sbapiserver.service.login.LoginService;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/login")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserLoginController {
    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public void login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest req)throws Exception{
    }

    @PostMapping("/logout")
    public ResultResponse<Void> logout(HttpServletRequest req){
        String accessToken = jwtUtil.getJwtToken(req);
        loginService.logout(jwtUtil.getClaims(accessToken).getSubject());

        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.LOGOUT_SUCCESS)
                .build();
    }

}
