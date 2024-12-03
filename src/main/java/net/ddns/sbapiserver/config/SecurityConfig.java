package net.ddns.sbapiserver.config;

import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import net.ddns.sbapiserver.security.CustomAuthenticationProvider;
import net.ddns.sbapiserver.security.JwtTokenAuthenticationFilter;
import net.ddns.sbapiserver.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final StaffRepository staffRepository;
    private final ClientRepository clientRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter() throws Exception{
        JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter = new JwtTokenAuthenticationFilter(jwtUtil, clientRepository, staffRepository);
        jwtTokenAuthenticationFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
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
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/api/login").permitAll()
                                .anyRequest().authenticated());

        httpSecurity.addFilterBefore(jwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
