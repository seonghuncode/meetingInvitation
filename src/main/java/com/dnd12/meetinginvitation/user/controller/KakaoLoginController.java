package com.dnd12.meetinginvitation.user.controller;

import com.dnd12.meetinginvitation.common.ApiResponse;
import com.dnd12.meetinginvitation.user.dto.LoginResponse;
import com.dnd12.meetinginvitation.user.dto.TokenDto;
import com.dnd12.meetinginvitation.user.service.KakaoLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginController {

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
            String redirectUrl = String.format(
                    "http://localhost:3000/auth/kakao?accessToken=%s&refreshToken=%s&userId=%s&name=%s&profileImageUrl=%s",
                    loginResponse.getAccessToken(),
                    loginResponse.getRefreshToken(),
                    loginResponse.getUserId(),
                    encodedName,
                    loginResponse.getProfileImageUrl()
            );
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            log.error("Redirect failed: ",e);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenDto>> refresh(@RequestHeader("Authorization") String refreshToken) {
        try {
            refreshToken = refreshToken.substring(7);
            TokenDto newTokens = kakaoLoginService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(ApiResponse.success(newTokens));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("액세스 토큰 재발급 실패"));
        }
    }
}
