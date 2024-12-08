package net.ddns.sbapiserver.service.authentication;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.exception.custom.UserNotValidException;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.security.UnifiedUserDetails;
import net.ddns.sbapiserver.security.UserType;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ClientRepository clientRepository;

    /**
     *
     * @param authentication UserDetails -> role, username 을 반환
     * @param userId Clients or Staffs Primary Key
     * @return 관리자 or 사용자 본인 -> true or false
     */
    public boolean isOwner(Authentication authentication, int userId){
        UnifiedUserDetails userDetails = (UnifiedUserDetails) authentication.getPrincipal();
        String role = userDetails.getUserType().getRole();

        isUserAuthenticated(authentication, role);

        if(role.equals(UserType.CLIENT.getRole())){
           return isClientOwner(userDetails, userId);
        }else if(role.equals(UserType.STAFF.getRole())){
            return true; // STAFF 는 모든 권한 존재
        }else {
            throw new UserNotValidException(ErrorCode.ACCESS_DENIED);
        }
    }

    private void isUserAuthenticated(Authentication authentication,String role){
        if(!authentication.isAuthenticated() || role == null){
            throw new UserNotValidException(ErrorCode.ACCESS_DENIED);
        }
    }

    private boolean isClientOwner(UnifiedUserDetails userDetails, int userId){
        Clients clients = clientRepository.findClientsByClientName(userDetails.getUsername());
        if (clients.getClientId() == userId){
            return true;
        }else {
            throw new UserNotValidException(ErrorCode.USER_IS_NOT_OWNER);
        }
    }

}
