package net.ddns.sbapiserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.common.ResponseWrapper;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.common.code.SuccessCode;
import net.ddns.sbapiserver.common.response.ResultResponse;
import net.ddns.sbapiserver.domain.dto.UserLoginDto;
import net.ddns.sbapiserver.domain.dto.common.ClientsDto;
import net.ddns.sbapiserver.domain.dto.staff.StaffDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.exception.error.ErrorResponse;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import net.ddns.sbapiserver.service.authentication.TokenStorageService;
import net.ddns.sbapiserver.security.dto.JwtToken;
import net.ddns.sbapiserver.service.login.LoginService;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * Login 시에만 사용되는 Filter
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final ClientRepository clientRepository;
    private final StaffRepository staffRepository;
    private final LoginService loginService;
    private final TokenStorageService tokenStorageService;
    private final ResponseWrapper responseWrapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            UserLoginDto userLoginDto = new ObjectMapper().readValue(request.getInputStream(), UserLoginDto.class);

            Clients clients = clientRepository.findClientsByClientName(userLoginDto.getUserId());
            Staffs staffs = staffRepository.findStaffsByStaffUserId(userLoginDto.getUserId());

            if(clients != null || staffs != null){
                request.setAttribute("checkSum", userLoginDto.getCheckSum());
                return getAuthenticationManager().authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userLoginDto.getUserId(),
                                userLoginDto.getUserPw(),
                                null
                        )
                );
            }
            else {
                throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
            }
        }catch (IOException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }
    /**
     *
     * attemptAuthentication -> Success -> successfulAuthentication
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        UnifiedUserDetails userDetails = (UnifiedUserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String role = userDetails.getUserType().getRole();
        String refreshToken = userDetails.getRefreshToken();

        /**
         * 사용자의 Id 로 생성된 토큰이 레디스에 존재 --> 이미 로그인 중인 사용자
         * checkSum --> 1 : 이미 로그인중인 사용자의 accessToken을 삭제하고 업데이트
         * checkSum --> 0 : 로그인 거부
         */
        int checksum = (int)request.getAttribute("checkSum");
        if(loginService.isUserLoggedIn(username)){
            if(checksum == 1){
                loginService.deleteAccessToken(username);
            }else{
                ErrorResponse errorResponse = new ErrorResponse(ErrorCode.USER_ALREADY_LOGGED_ERROR);
                responseWrapper.convertObjectToResponse(response, errorResponse);
                return;
            }
        }
            /** refreshToken 의 존재, 우효한지 확인
             * 존재하고 유효 -> 클라이언트에게 액세스토큰 발급
             * 그 외의 경우 -> RefreshToken DB 저장 -> 액세스토큰 발급
             */
            if (refreshToken == null || !jwtUtil.validToken(refreshToken)) {
                tokenStorageService.saveNewRefreshToken(username, role);
            }
            JwtToken jwtToken = generateTokenByIdAndRole(username, role);
            //Redis 에 username , AccessToken 저장
            loginService.storeAccessToken(username, jwtToken.getAccessToken(), role);

        ResultResponse<Object> tokenResponse = ResultResponse.<Object>successResponse()
                .successCode(SuccessCode.LOGIN_SUCCESS)
                .result(jwtToken)
                .build();
        responseWrapper.convertObjectToResponse(response, tokenResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.USER_NOT_FOUND_ERROR);
        responseWrapper.convertObjectToResponse(response, errorResponse);
    }

    protected JwtToken generateTokenByIdAndRole(String username, String role){
        JwtToken jwtToken = jwtUtil.generateToken(username, role);
        if(role.equals(UserType.CLIENT.getRole())){
            Clients clients = clientRepository.findClientsByClientName(username);
            jwtToken.setUser(ClientsDto.Result.of(clients));
        }else{
            Staffs staffs = staffRepository.findStaffsByStaffUserId(username);
            jwtToken.setUser(StaffDto.Result.of(staffs));
        }
        return jwtToken;
    }
}
