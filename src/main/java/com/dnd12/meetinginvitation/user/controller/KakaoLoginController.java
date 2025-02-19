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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginController {

    @Value("${frontend.url}")
    private String frontendUrl;
    private final KakaoLoginService kakaoLoginService;

//    @GetMapping("/kakao_login")
//    public ResponseEntity<ApiResponse<LoginResponse>> kakaoLogin(@RequestParam("code") String code) {
//        LoginResponse loginResponse = kakaoLoginService.handleKakaoLogin(code);
//        return ResponseEntity.ok(ApiResponse.success(loginResponse));
//    }

    @GetMapping("/kakao_login")
    public void kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {
        try {
            LoginResponse loginResponse = kakaoLoginService.handleKakaoLogin(code);
            String encodedName = URLEncoder.encode(loginResponse.getName(), "UTF-8");

            //쿠키 생성
            Cookie accessTokenCookie = new Cookie("token", loginResponse.getAccessToken());
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(true); //HTTPS에서만 전송
            accessTokenCookie.setPath("/"); //모든 경로에서 접근 가능
            accessTokenCookie.setMaxAge(3600); // 쿠키 유효시간 설정(1시간)

            String sameSite = "None";
            String cookieValue = String.format("%s; SameSite=%s", accessTokenCookie.toString(), sameSite);
            response.setHeader("Set-Cookie", cookieValue);

            // 디버깅용 로그
            log.info("KakaoLogin Setting cookie with SameSite=None: {}", cookieValue);

            //응답에 쿠키 추가
            response.addCookie(accessTokenCookie);

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
