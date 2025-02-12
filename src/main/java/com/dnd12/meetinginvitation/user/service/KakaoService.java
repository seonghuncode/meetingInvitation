package com.dnd12.meetinginvitation.user.service;

import com.dnd12.meetinginvitation.jwt.JwtTokenProvider;
import com.dnd12.meetinginvitation.user.dto.KakaoLogoutResponse;
import com.dnd12.meetinginvitation.user.dto.KakaoTokenResponseDto;
import com.dnd12.meetinginvitation.user.dto.KakaoUserInfoDto;
import com.dnd12.meetinginvitation.user.dto.LoginResponse;
import com.dnd12.meetinginvitation.user.entity.User;
import com.dnd12.meetinginvitation.user.repository.UserRepository;
import com.dnd12.meetinginvitation.util.RedisUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.client-secret}")
    private String clientSecret;

    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    private final long tokenValidityInMilliseconds = 1000L * 60 * 60 * 3;

    public LoginResponse handleKakaoLogin(String code) {
        // 카카오 액세스 토큰 받기
        KakaoTokenResponseDto tokenResponse = getAccessTokenFromKakao(code);

        //카카오 AccessToken
        String kakaoAccessToken = tokenResponse.getAccessToken();

        // 카카오 사용자 정보 받기
        KakaoUserInfoDto userInfo = getUserInfo(kakaoAccessToken);

        // 이메일로 기존 회원인지 확인
        Optional<User> existingUser = userRepository.findByEmail(userInfo.kakaoAccount.getEmail());

        User user;

        //새로운 회원인 경우 DB에 저장
        if (existingUser.isPresent()) {
            user = existingUser.get();

        } else {
            user = User.builder()
                    .email(userInfo.kakaoAccount.getEmail())
                    .Id(userInfo.getId())
                    .name(userInfo.kakaoAccount.profile.getNickName())
                    .profileImageUrl(userInfo.kakaoAccount.profile.getProfileImageUrl())
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
        }
        //JWT 토큰 생성
        String accessToken = jwtTokenProvider.createToken(user.getEmail());

        //레디스에 카카오 AccessToken 저장
        redisUtil.setValuesWithTimeout("AT:" + user.getEmail(),kakaoAccessToken,tokenValidityInMilliseconds);

        //응답 생성
        return LoginResponse.builder()
                .accessToken(accessToken)
                .email(user.getEmail())
                .name(user.getName())
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .build();

    }

    public KakaoTokenResponseDto getAccessTokenFromKakao(String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();
        return kakaoTokenResponseDto;
    }

    // 받아온 토큰을 통해 사용자 정보 가져오기
    public KakaoUserInfoDto getUserInfo(String accessToken) {

        KakaoUserInfoDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(KakaoUserInfoDto.class)
                .block();

        return userInfo;
    }

    //로그아웃
    public void logout(String token) {
        // 액세스 토큰 무효화
        invalidateToken(token);

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



