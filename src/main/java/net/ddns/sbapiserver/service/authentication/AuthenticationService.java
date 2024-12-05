package net.ddns.sbapiserver.service.authentication;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    public boolean isOwner(String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.get
    }
}
