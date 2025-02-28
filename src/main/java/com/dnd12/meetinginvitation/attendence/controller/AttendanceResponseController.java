package com.dnd12.meetinginvitation.attendence.controller;

import com.dnd12.meetinginvitation.attendence.dto.AttendanceRequest;
import com.dnd12.meetinginvitation.attendence.dto.UserAttendanceRequest;
import com.dnd12.meetinginvitation.attendence.service.AttendanceService;
import com.dnd12.meetinginvitation.common.ApiResponse;
import com.dnd12.meetinginvitation.user.dto.LoginResponse;
import com.dnd12.meetinginvitation.user.service.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attendance")
@Slf4j
public class AttendanceResponseController {

    @Value("${frontend.url}")
    private String frontendUrl;
    private final AttendanceService attendanceService;
    private final KakaoLoginService kakaoLoginService;

    private void setTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .path("/")
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .maxAge(Duration.ofHours(3))
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
        log.info("Setting cookie: {}", cookie.toString());
    }

    //비회원 로그인 후 응답
    @Operation(summary = "비화원 로그인", description = "state : ATTENDING(참석), NOT_ATTENDING(불참), PENDING(보류/미정)")
    @PostMapping("/nonUser/response")
    public ResponseEntity<ApiResponse<Void>> AttendanceResponse(@RequestBody AttendanceRequest request) {
        attendanceService.saveAttendance(request);
        return ResponseEntity.ok(ApiResponse.success("비회원 참석자 응답 성공"));
    }

    @Operation(summary = "화원 로그인", description = "state : ATTENDING(참석), NOT_ATTENDING(불참), PENDING(보류/미정)")
    @PostMapping("/user/response")
    public ResponseEntity<ApiResponse<Void>> UserAttendanceResponse(@RequestBody UserAttendanceRequest userAttendanceRequest) {
        attendanceService.saveUserAttendance(userAttendanceRequest);
        return ResponseEntity.ok(ApiResponse.success("회원 참석자 응답 성공"));
    }

    //회원 로그인 후 응답
    @GetMapping("/login")
    public void attendanceLogin(@RequestParam("code") String code, HttpServletResponse response) {
        try {
            LoginResponse loginResponse = kakaoLoginService.handleKakaoLogin(code);
            String encodedName = URLEncoder.encode(loginResponse.getName(), "UTF-8");

            //쿠키 생성
            setTokenCookie(response, loginResponse.getAccessToken());

//            Cookie accessTokenCookie = new Cookie("token", loginResponse.getAccessToken());
//            accessTokenCookie.setHttpOnly(true);
//            accessTokenCookie.setSecure(true); //HTTPS에서만 전송
//            accessTokenCookie.setPath("/"); //모든 경로에서 접근 가능
//            accessTokenCookie.setMaxAge(3600); // 쿠키 유효시간 설정(1시간)
//
//            String sameSite = "None";
//            String cookieValue = String.format("%s; SameSite=%s", accessTokenCookie.toString(), sameSite);
//            response.setHeader("Set-Cookie", cookieValue);
//
//            // 디버깅용 로그
//            log.info("Attendance Setting cookie with SameSite=None: {}", cookieValue);
//
//            //응답에 쿠키 추가
//            response.addCookie(accessTokenCookie);

            String redirectUrl = UriComponentsBuilder
                    .fromUriString(frontendUrl)
                    .path("invitation/answer")
                    .queryParam("userId", loginResponse.getUserId())
                    .queryParam("name", encodedName)
                    .queryParam("profileImageUrl", loginResponse.getProfileImageUrl())
                    .queryParam("email", loginResponse.getEmail())
                    .queryParam("token", loginResponse.getAccessToken())
                    .build()
                    .toUriString();

            log.info("redirectUrl:{}",redirectUrl);

            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            log.error("Redirect failed: ", e);
        }
    }
}
