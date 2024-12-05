package net.ddns.sbapiserver.service.authentication;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenStorageService {
    private final ClientRepository clientRepository;
    private final StaffRepository staffRepository;
    private final JwtUtil jwtUtil;

    public void saveRefreshToken(String userId, String refreshToken, String role) {
        if(!jwtUtil.validToken(refreshToken)){
            String createRefreshToken = jwtUtil.generateRefreshToken(userId);
            if (role.equals("ROLE_CLIENT")) {
                saveClientRefreshToken(userId, createRefreshToken);
            } else if (role.equals("ROLE_STAFF")) {
                saveStaffRefreshToken(userId, createRefreshToken);
            } else {
                throw new IllegalArgumentException("존재하지 않는 ROLE");
            }
        }
    }

    private void saveClientRefreshToken(String userId, String refreshToken) {
        Clients client = clientRepository.findClientsByClientName(userId);
        client.setClientRefreshToken(refreshToken);
        clientRepository.save(client);
    }

    private void saveStaffRefreshToken(String userId, String refreshToken) {
        Staffs staff = staffRepository.findStaffsByStaffUserId(userId);
        staff.setStaffRefreshToken(refreshToken);
        staffRepository.save(staff);
    }
}
