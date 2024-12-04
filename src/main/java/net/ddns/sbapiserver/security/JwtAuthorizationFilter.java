package net.ddns.sbapiserver.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UnifiedUserDetailsService unifiedUserDetailsService;

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("AuthorizationFilter 작동");

        String token = jwtUtil.getJwtToken(request);

        if(token != null && jwtUtil.validToken(token)){
            Claims claims = jwtUtil.getClaims(token);
            try{
                setAuthentication(claims.getSubject());
            }catch (Exception e){
                throw new ResponseStatusException(NOT_FOUND, "요청하신 유저가 없습니다.");
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String userId){
        UnifiedUserDetails unifiedUserDetails = (UnifiedUserDetails) unifiedUserDetailsService.loadUserByUsername(userId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(unifiedUserDetails, null, unifiedUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
