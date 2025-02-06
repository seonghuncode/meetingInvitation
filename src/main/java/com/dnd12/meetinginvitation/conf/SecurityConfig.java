package com.dnd12.meetinginvitation.conf;

import com.dnd12.meetinginvitation.common.JwtAuthenticationFilter;
import com.dnd12.meetinginvitation.common.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/sign-up", "/login", "/error", "/kakao_login", "/oauth2/authorization/kakao").permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)

                .oauth2Login(oAuth -> oAuth
                        .loginPage("/oauth2/authorization/kakao"));

        return http.build();
    }
}
