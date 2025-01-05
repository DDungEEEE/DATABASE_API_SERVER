package net.ddns.sbapiserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.common.ResponseWrapper;
import net.ddns.sbapiserver.domain.dto.UserLoginDto;
import net.ddns.sbapiserver.domain.dto.common.ClientsDto;
import net.ddns.sbapiserver.domain.dto.staff.StaffDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import net.ddns.sbapiserver.service.authentication.TokenStorageService;
import net.ddns.sbapiserver.security.dto.JwtToken;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final ClientRepository clientRepository;
    private final StaffRepository staffRepository;
    private final TokenStorageService tokenStorageService;
    private final ResponseWrapper responseWrapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            UserLoginDto userLoginDto = new ObjectMapper().readValue(request.getInputStream(), UserLoginDto.class);

            Clients clients = clientRepository.findClientsByClientName(userLoginDto.getUserId());
            Staffs staffs = staffRepository.findStaffsByStaffUserId(userLoginDto.getUserId());

            if(clients != null || staffs != null){
                return getAuthenticationManager().authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userLoginDto.getUserId(),
                                userLoginDto.getUserPw(),
                                null
                        )
                );
            }
            else {
                throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
            }
        }catch (IOException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }
    /**
     *
     * attemptAuthentication -> Success -> successfulAuthentication
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        UnifiedUserDetails userDetails = (UnifiedUserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String role = userDetails.getUserType().getRole();
        String refreshToken = userDetails.getRefreshToken();


        tokenStorageService.saveRefreshToken(username, refreshToken, role);
        JwtToken jwtToken = jwtUtil.generateToken(username, role);
        if(role.equals(UserType.CLIENT.getRole())){
            Clients clients = clientRepository.findClientsByClientName(username);
            jwtToken.setUser(ClientsDto.Result.of(clients));
        }else{
            Staffs staffs = staffRepository.findStaffsByStaffUserId(username);
            jwtToken.setUser(StaffDto.Result.of(staffs));
        }
        responseWrapper.convertObjectToResponse(response, jwtToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(403);
    }
}
