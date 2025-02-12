package com.dnd12.meetinginvitation.user.controller;

import com.dnd12.meetinginvitation.common.ApiResponse;
import com.dnd12.meetinginvitation.user.dto.KakaoUserInfoDto;
import com.dnd12.meetinginvitation.user.dto.LoginResponse;
import com.dnd12.meetinginvitation.user.service.KakaoService;
import com.dnd12.meetinginvitation.user.dto.KakaoTokenResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginController {

    private final KakaoService kakaoService;

    @GetMapping("/kakao_login")
    public ResponseEntity<ApiResponse<LoginResponse>> kakaoLogin(@RequestParam("code") String code) {
        LoginResponse loginResponse = kakaoService.handleKakaoLogin(code);
        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }

    @PostMapping("/kakao_logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.substring(7);
            kakaoService.logout(token);

            return ResponseEntity.ok(
                    ApiResponse.success("로그아웃이 성공적으로 처리되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("로그아웃 처리 중 오류가 발생했습니다."));
        }


    }
}
