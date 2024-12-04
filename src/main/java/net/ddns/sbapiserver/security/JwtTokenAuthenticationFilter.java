package net.ddns.sbapiserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.sbapiserver.domain.dto.UserLoginDto;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.exception.custom.BusinessException;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import net.ddns.sbapiserver.service.helper.ServiceErrorHelper;
import net.ddns.sbapiserver.util.JwtToken;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtTokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final ClientRepository clientRepository;
    private final StaffRepository staffRepository;

    public JwtTokenAuthenticationFilter(JwtUtil jwtUtil, ClientRepository clientRepository, StaffRepository staffRepository){
        this.jwtUtil = jwtUtil;
        this.clientRepository = clientRepository;
        this.staffRepository = staffRepository;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            UserLoginDto userLoginDto = new ObjectMapper().readValue(request.getInputStream(), UserLoginDto.class);
            log.error("JwtTokenAuthenticationFilter 작동");

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
        String username = ((UnifiedUserDetails) authentication.getPrincipal()).getUsername();
        String role = ((UnifiedUserDetails) authentication.getPrincipal()).getUserType().getRole();

        String accessToken = jwtUtil.generateAccessToken(username, role);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JwtToken jwtToken = JwtToken.builder()
                .accessToken(accessToken)
                .role(role)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String responseJwtToken = objectMapper.writeValueAsString(jwtToken);

        response.getWriter().write(responseJwtToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(403);
    }
}
