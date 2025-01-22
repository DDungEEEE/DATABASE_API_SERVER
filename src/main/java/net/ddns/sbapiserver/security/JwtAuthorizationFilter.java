package net.ddns.sbapiserver.security;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.common.ResponseWrapper;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.exception.error.ErrorResponse;
import net.ddns.sbapiserver.service.login.LoginService;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Base64;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Login 후 API 요청 시 사용되는 Method
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UnifiedUserDetailsService unifiedUserDetailsService;
    private final LoginService loginService;
    private final ResponseWrapper responseWrapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("AuthorizationFilter[OncePerRequestFilter] 작동");

        if (request.getRequestURI().startsWith("/api/user/getAccessToken")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = jwtUtil.getJwtToken(request);
        try {
            log.error("token 값 : {}", token);
            if (token != null) {
                // 토큰 유효성 검증
                if(!jwtUtil.validToken(token)){
                    ErrorResponse errorResponse = new ErrorResponse(ErrorCode.TOKEN_EXPIRED_ERROR);
                    responseWrapper.convertObjectToResponse(response, errorResponse);
                    return;
                }

                Claims claims = jwtUtil.getClaims(token);
                String userId = claims.getSubject();

                boolean userLoginValid = loginService.isUserLoginValid(userId, token);

                if(!loginService.isUserLoggedIn(userId) || !userLoginValid){
                    ErrorResponse errorResponse = new ErrorResponse(ErrorCode.TOKEN_EXPIRED_ERROR);
                    responseWrapper.convertObjectToResponse(response, errorResponse);
                    return;
                }

                setAuthentication(claims.getSubject());
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e){
            log.error(e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
            responseWrapper.convertObjectToResponse(response, errorResponse);
            }
        }

    private void setAuthentication(String userId){
        UnifiedUserDetails unifiedUserDetails = (UnifiedUserDetails) unifiedUserDetailsService.loadUserByUsername(userId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(unifiedUserDetails, null, unifiedUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
