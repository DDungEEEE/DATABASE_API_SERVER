package net.ddns.sbapiserver.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.error("CustomAuthenticationProvider 작동중");
        String userId = authentication.getName();
        String userPwd = authentication.getCredentials().toString();

        UnifiedUserDetails unifiedUserDetails = (UnifiedUserDetails) customUserDetailsService.loadUserByUsername(userId);

        if(passwordEncoder.matches(userPwd, unifiedUserDetails.getPassword())){
                return new UsernamePasswordAuthenticationToken(unifiedUserDetails.getUsername(), userPwd, unifiedUserDetails.getAuthorities());
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
