package com.dnd12.meetinginvitation.conf;

import com.dnd12.meetinginvitation.jwt.JwtAuthenticationFilter;
import com.dnd12.meetinginvitation.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
         feature/login_attendance_response
                        .requestMatchers("/", "/sign-up", "/login", "/error", "/kakao_login", "/oauth2/authorization/kakao","/attendance/response", "/attendance/login", "/attendance/nonUser/response",  "/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
         develop
                        .anyRequest().authenticated())

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate),
                        UsernamePasswordAuthenticationFilter.class)

                .oauth2Login(oAuth -> oAuth
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorization"))
                        .successHandler(((request, response, authentication) -> {
                            String registrationId = ((OAuth2AuthenticationToken) authentication)
                                    .getAuthorizedClientRegistrationId();

                            if ("kakao-attendance".equals(registrationId)) {
                                response.sendRedirect("/attendance/login");
                            } else {
                                response.sendRedirect("/kakao_login");
                            }
                        })));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
