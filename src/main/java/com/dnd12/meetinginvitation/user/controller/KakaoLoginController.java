package com.dnd12.meetinginvitation.user.controller;

import com.dnd12.meetinginvitation.common.ApiResponse;
import com.dnd12.meetinginvitation.user.dto.KakaoUserInfoDto;
import com.dnd12.meetinginvitation.user.dto.LoginResponse;
import com.dnd12.meetinginvitation.user.service.KakaoService;
import com.dnd12.meetinginvitation.user.dto.KakaoTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoService kakaoService;

//    @GetMapping("/kakao_login")
//    public LoginResponse kakaoLogin(@RequestParam("code") String code) {
//        LoginResponse loginResponse = kakaoService.handleKakaoLogin(code);
//        return loginResponse;
//    }

    @GetMapping("/kakao_login")
    public ResponseEntity<ApiResponse<LoginResponse>> kakaoLogin(@RequestParam("code") String code) {
        LoginResponse loginResponse = kakaoService.handleKakaoLogin(code);
        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }
}
