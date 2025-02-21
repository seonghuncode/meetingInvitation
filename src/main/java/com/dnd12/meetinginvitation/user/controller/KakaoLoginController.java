package com.dnd12.meetinginvitation.user.controller;

import com.dnd12.meetinginvitation.common.ApiResponse;
import com.dnd12.meetinginvitation.user.dto.LoginResponse;
import com.dnd12.meetinginvitation.user.dto.TokenDto;
import com.dnd12.meetinginvitation.user.service.KakaoLoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginController {

    @Value("${frontend.url}")
    private String frontendUrl;
    private final KakaoLoginService kakaoLoginService;

    private void setTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .path("/")
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .maxAge(Duration.ofHours(1))
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
        log.info("Setting cookie: {}", cookie.toString());
    }

    @GetMapping("/kakao_login")
    public void kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {
        try {
            LoginResponse loginResponse = kakaoLoginService.handleKakaoLogin(code);
            String encodedName = URLEncoder.encode(loginResponse.getName(), "UTF-8");

            //쿠키 생성
//            Cookie accessTokenCookie = new Cookie("token", loginResponse.getAccessToken());
//            accessTokenCookie.setHttpOnly(true);
//            accessTokenCookie.setSecure(true); //HTTPS에서만 전송
//            accessTokenCookie.setPath("/"); //모든 경로에서 접근 가능
//            accessTokenCookie.setMaxAge(3600); // 쿠키 유효시간 설정(1시간)
//
//            //응답에 쿠키 추가
//            response.addCookie(accessTokenCookie);
            setTokenCookie(response, loginResponse.getAccessToken());

            String redirectUrl = String.format(
                    "%s/auth/kakao?userId=%s&name=%s&profileImageUrl=%s&email=%s",
                    frontendUrl,
                    loginResponse.getUserId(),
                    encodedName,
                    loginResponse.getProfileImageUrl(),
                    loginResponse.getEmail()
            );

            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            log.error("Redirect failed: ", e);
        }
    }

    // 액세스 토큰 재발급 API => 보류
//    @PostMapping("/refresh")
//    public ResponseEntity<ApiResponse<TokenDto>> refresh(@RequestHeader("Authorization") String refreshToken) {
//        try {
//            refreshToken = refreshToken.substring(7);
//            TokenDto newTokens = kakaoLoginService.refreshAccessToken(refreshToken);
//            return ResponseEntity.ok(ApiResponse.success(newTokens));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("액세스 토큰 재발급 실패"));
//        }
//    }
}
