package net.ddns.sbapiserver.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.common.swagger.ApiErrorCodeExamples;
import net.ddns.sbapiserver.domain.dto.UserLoginDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.exception.error.custom.BusinessException;
import net.ddns.sbapiserver.security.UserType;
import net.ddns.sbapiserver.security.dto.JwtToken;
import net.ddns.sbapiserver.security.dto.RefreshTokenReq;
import net.ddns.sbapiserver.service.common.ClientService;
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
    private final ClientService clientService;
    private final JwtUtil jwtUtil;

    @ApiErrorCodeExamples(value = {ErrorCode.USER_NOT_LOGGED_ERROR, ErrorCode.TOKEN_EXPIRED_ERROR, ErrorCode.USER_ALREADY_LOGGED_ERROR})
    @Operation(summary = "사용자 로그인", description = "checkSum --> 강제 로그인 여부 1 : 강제 로그인 0 : 강제 로그인 X")
    @PostMapping("/login")
    public void login(@RequestBody @Parameter UserLoginDto userLoginDto, HttpServletRequest req)throws Exception{
    }

    @Operation(summary = "만료된 액세스토큰으로 새로운 액세스토큰 발급", description = "Header에 AccessToken 값 필요")
    @PostMapping("/getAccessToken")
    public ResultResponse<JwtToken> refreshByExpiredAccessToken(HttpServletRequest req){

        /**
         * Test Login Logic -> 만료된 accessToken 으로 사용자 정보 강제 추출 -> 새로운 accessToken 발급
         */
        String expiredJwtToken = jwtUtil.getJwtToken(req);
        String role = jwtUtil.extractRole(expiredJwtToken);
        Claims claims = jwtUtil.getClaims(expiredJwtToken);
        String userId = claims.getSubject();

        JwtToken jwtToken = jwtUtil.generateToken(userId, role);
        loginService.deleteAccessToken(userId);
        loginService.storeAccessToken(userId, jwtToken.getAccessToken(), role);

        return ResultResponse.<JwtToken>successResponse()
                .result(jwtToken)
                .successCode(SuccessCode.SELECT_SUCCESS)
                .build();
    }

    @PostMapping("/getAccessTokenByRe")
    public ResultResponse<JwtToken> createNewAcTokenByReToken(@RequestBody RefreshTokenReq req){
        Clients findClients = clientService.findClientByUserName(req.getClientUsername());
        boolean validToken = jwtUtil.validToken(req.getClientRefreshToken());

        if(!validToken || !req.getClientRefreshToken().equals(findClients.getClientRefreshToken())){
            ErrorCode tokenExpiredError = ErrorCode.TOKEN_EXPIRED_ERROR;
            throw new BusinessException(tokenExpiredError, tokenExpiredError.getReason());
        }

        JwtToken jwtToken = jwtUtil.generateAcTokenByReToken(req.getClientRefreshToken());
        String clientName = req.getClientUsername();

        loginService.deleteAccessToken(clientName);
        loginService.storeAccessToken(clientName, jwtToken.getAccessToken(), UserType.CLIENT.getRole());

        return ResultResponse.<JwtToken>successResponse()
                .result(jwtToken)
                .successCode(SuccessCode.UPDATE_SUCCESS)
                .build();
    }

    @Operation(summary = "사용자 로그아웃", description = "Header 값에 AccessToken 필요")
    @PostMapping("/logout")
    public ResultResponse<Void> logout(HttpServletRequest req){
        String accessToken = jwtUtil.getJwtToken(req);
        loginService.logout(jwtUtil.getClaims(accessToken).getSubject());
        return ResultResponse.<Void>successResponse()
                .successCode(SuccessCode.LOGOUT_SUCCESS)
                .build();
    }

}
