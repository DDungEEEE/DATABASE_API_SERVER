package net.ddns.sbapiserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.UserLoginDto;
import net.ddns.sbapiserver.service.common.UserLoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/login")
@RestController
@RequiredArgsConstructor
public class UserLoginController {
    private final UserLoginService userLoginService;

    @PostMapping
    public void login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest req){
//        userLoginService.userUnifiedLogin(userLoginDto.getUserId(), userLoginDto.getUserPw());
        System.out.println("로그인 성공!!!!!!!!!!!");
    }
}
