package net.ddns.sbapiserver.service.authentication;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.exception.custom.UserNotValidException;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenVerificationService {
    private final ClientRepository clientRepository;
    private final StaffRepository staffRepository;
    private final JwtUtil jwtUtil;

    public boolean isRefreshTokenValid(String userId, String role){
        String storedRefreshToken = findStoredRefreshToken(userId, role);
        return storedRefreshToken != null && isTokenExpired(storedRefreshToken);
    }

    private String findStoredRefreshToken(String userId, String role)  {
        return switch (role){
            case "ROLE_CLIENT" -> clientRepository.findClientsByClientName(userId).getClientRefreshToken();
            case "ROLE_STAFF" -> staffRepository.findStaffsByStaffUserId(userId).getStaffRefreshToken();
            default -> throw new UserNotValidException(ErrorCode.ROLE_NOT_AUTHORIZED, ErrorCode.ROLE_NOT_AUTHORIZED.getReason());
        };
    }

    private boolean isTokenExpired(String token){
        return jwtUtil.validToken(token);
    }
}
