package net.ddns.sbapiserver.security;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnifiedUserDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;
    private final StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Staffs findStaffs = staffRepository.findStaffsByStaffUserId(username);
        Clients findClients = clientRepository.findClientsByClientName(username);

        if(findClients != null){
            return new UnifiedUserDetails(findClients);
        }else if(findStaffs != null){
            return new UnifiedUserDetails(findStaffs);
        }else {
            throw new UsernameNotFoundException("not found");
        }
    }
}
