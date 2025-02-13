package com.dnd12.meetinginvitation.user.controller;

import com.dnd12.meetinginvitation.common.ApiResponse;
import com.dnd12.meetinginvitation.user.service.KakaoLoginService;
import com.dnd12.meetinginvitation.user.service.KakaoLogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoLogoutController {

    private final KakaoLogoutService kakaoLogoutService;


    @PostMapping("/kakao_logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.substring(7);
            kakaoLogoutService.logout(token);

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
