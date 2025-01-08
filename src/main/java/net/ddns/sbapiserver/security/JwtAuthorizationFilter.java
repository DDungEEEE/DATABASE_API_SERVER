package net.ddns.sbapiserver.security;

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

        String token = jwtUtil.getJwtToken(request);
        try {
            if (token != null && jwtUtil.validToken(token)) {
                Claims claims = jwtUtil.getClaims(token);
                String userId = claims.getSubject();
                boolean userLoginValid = loginService.isUserLoginValid(userId, token);
                // accessToken이 레디스에 저장되어있는지 검증
                if (!userLoginValid) {
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
                throw new ResponseStatusException(NOT_FOUND, "요청하신 유저가 없습니다.");
            }
        }

    private void setAuthentication(String userId){
        UnifiedUserDetails unifiedUserDetails = (UnifiedUserDetails) unifiedUserDetailsService.loadUserByUsername(userId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(unifiedUserDetails, null, unifiedUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
