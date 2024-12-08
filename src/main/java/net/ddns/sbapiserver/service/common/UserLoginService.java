package net.ddns.sbapiserver.service.common;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import net.ddns.sbapiserver.security.UnifiedUserDetails;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final JwtUtil jwtUtil;
    private final ClientRepository clientRepository;
    private final StaffRepository staffRepository;

    public void userUnifiedLogin(String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UnifiedUserDetails userDetails = (UnifiedUserDetails) authentication.getPrincipal();

        String role = userDetails.getUserType().getRole();
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        if(role.equals("ROLE_CLIENT")){
            Clients clients = clientRepository.findClientsByClientName(userId);
            clients.setClientRefreshToken(refreshToken);
        }else{
            Staffs staffs = staffRepository.findStaffsByStaffUserId(userId);
            staffs.setStaffRefreshToken(refreshToken);
        }
    }
}
