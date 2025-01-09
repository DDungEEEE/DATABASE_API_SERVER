package net.ddns.sbapiserver.config;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.common.ResponseWrapper;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import net.ddns.sbapiserver.security.JwtAuthorizationFilter;
import net.ddns.sbapiserver.security.JwtTokenAuthenticationFilter;
import net.ddns.sbapiserver.security.UnifiedUserDetails;
import net.ddns.sbapiserver.security.UnifiedUserDetailsService;
import net.ddns.sbapiserver.service.authentication.TokenStorageService;
import net.ddns.sbapiserver.service.login.LoginService;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final StaffRepository staffRepository;
    private final ClientRepository clientRepository;
    private final UnifiedUserDetailsService unifiedUserDetailsService;
    private final TokenStorageService tokenStorageService;
    private final ResponseWrapper responseWrapper;
    private final LoginService loginService;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter(jwtUtil, unifiedUserDetailsService, loginService, responseWrapper);
    }

    @Bean
    public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter() throws Exception{
        JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter = new JwtTokenAuthenticationFilter(jwtUtil, clientRepository, staffRepository, loginService,tokenStorageService, responseWrapper);
        jwtTokenAuthenticationFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        jwtTokenAuthenticationFilter.setFilterProcessesUrl("/api/user/login");
        return jwtTokenAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/v1/**").authenticated()
                                .requestMatchers("/api/v1/client").permitAll()
                                .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/api/user/login").permitAll()
                                .anyRequest().authenticated());

        httpSecurity.addFilterBefore(jwtAuthorizationFilter(), jwtTokenAuthenticationFilter().getClass());
        httpSecurity.addFilterBefore(jwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
