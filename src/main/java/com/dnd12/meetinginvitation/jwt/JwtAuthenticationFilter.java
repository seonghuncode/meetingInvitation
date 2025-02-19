package com.dnd12.meetinginvitation.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //쿠키에서 토큰 추출
        String token = null;
        Cookie[] cookies = request.getCookies();



        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        } else {
            log.info("No cookies found in request");
        }

        log.info("token 확인!!!!{}", token);

//        String token = resolveToken(request);

        if (token != null) {
            //1. 레디스에서 블랙리스트 확인
            String blackListToken = redisTemplate.opsForValue().get("BLACKLIST_" + token);
            if (blackListToken != null) {
//                log.info("Blacklisted token attempted to access: {}", token);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token: Token has been logged out");
                return;  // 중요: 여기서 필터 체인 진행을 멈춤
            }
        }

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String userEmail = jwtTokenProvider.getUserEmail(token);
//            log.info("Extracted email: {}", userEmail);  // 이메일 로그

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            userEmail, //principal(사용자 식별자)
                            null, //credentials
                            Collections.emptyList()); //authorities
            SecurityContextHolder.getContext().setAuthentication(authentication);
//            log.info("Authentication set in SecurityContext");  // 인증 설정 로그
        }

        filterChain.doFilter(request, response);
    }

    //쿠키에 액세스 토큰을 담아서 전달받으므로 삭제
//    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
////        log.info("bearerToken: {}", bearerToken);
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
}
