package com.dnd12.meetinginvitation.user.service;

import com.dnd12.meetinginvitation.jwt.JwtTokenProvider;
import com.dnd12.meetinginvitation.user.dto.KakaoLogoutResponse;
import io.netty.handler.codec.http.HttpHeaderValues;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoLogoutService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    //로그아웃
    public void logout(HttpServletResponse response, String token) {
        // 액세스 토큰 무효화(블랙리스트 처리)
        invalidateToken(token);

        // 쿠키의 액세스 토큰 삭제
        Cookie cookie = new Cookie("AccessToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        response.addCookie(cookie);

        // 카카오 소셜 로그인 accessToken 삭제
        String userEmail = jwtTokenProvider.getUserEmail(token);
        String kakaoAccessToken = redisTemplate.opsForValue().get("AT:" + userEmail);

        KakaoLogoutResponse kakaoLogoutResponse = kakaoLogout(kakaoAccessToken);

    }

    // 액세스/리프레시 토큰 무효화
    private void invalidateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        String userEmail = jwtTokenProvider.getUserEmail(token);
        Long remainingTime = jwtTokenProvider.getExpirationTime(token);

        // 남은 시간 동안만 Redis에 저장
        if (remainingTime > 0) {
            // JWT 토큰 블랙리스트 처리
            redisTemplate.opsForValue()
                    .set("BLACKLIST_" + token, userEmail, remainingTime, TimeUnit.MILLISECONDS);
            log.info("User {} logged out successfully", userEmail);
        }
    }

    //카카오톡 토큰 만료
    public KakaoLogoutResponse kakaoLogout(String accessToken) {
        KakaoLogoutResponse kakaoLogoutResponse = WebClient.create(KAUTH_USER_URL_HOST)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v1/user/logout")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RuntimeException("카카오 로그아웃 실패"))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("카카오 서버 오류"))
                )
                .bodyToMono(KakaoLogoutResponse.class)
                .block();

        return kakaoLogoutResponse;
    }
}
