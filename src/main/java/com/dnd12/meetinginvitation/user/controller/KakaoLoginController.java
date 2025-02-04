package com.dnd12.meetinginvitation.user.controller;

import com.dnd12.meetinginvitation.user.service.KakaoService;
import com.dnd12.meetinginvitation.user.dto.KakaoTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoService kakaoService;

    //    @GetMapping("/kakao_login")
//    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code) {
//        String accessToken = kakaoService.getAccessTokenFromKakao(code);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
    @GetMapping("/kakao_login")
    public KakaoTokenResponseDto kakaoLogin(@RequestParam("code") String code) {
        KakaoTokenResponseDto accessToken = kakaoService.getAccessTokenFromKakao(code);
        return accessToken;
    }
}
