package net.ddns.sbapiserver.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {
    private final UnifiedUserDetailsService unifiedUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userId = authentication.getName();
        String userPwd = authentication.getCredentials().toString();

        UnifiedUserDetails unifiedUserDetails = (UnifiedUserDetails) unifiedUserDetailsService.loadUserByUsername(userId);

        log.error("여기까찌도 작동됨 인증 성공");
        System.out.println("비번 인증 결과: + " + passwordEncoder.matches(userPwd, unifiedUserDetails.getPassword()));;
        if(passwordEncoder.matches(userPwd, unifiedUserDetails.getPassword())){
            log.info("match완료");
                return new UsernamePasswordAuthenticationToken(unifiedUserDetails, userPwd, unifiedUserDetails.getAuthorities());
            }else{
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            }
    }

    //AuthenticationProvider is supports UsernamePasswordAuthenticationToken
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
