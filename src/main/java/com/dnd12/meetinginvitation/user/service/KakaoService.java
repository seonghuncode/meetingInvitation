package com.dnd12.meetinginvitation.user.service;

import com.dnd12.meetinginvitation.common.JwtTokenProvider;
import com.dnd12.meetinginvitation.user.dto.KakaoTokenResponseDto;
import com.dnd12.meetinginvitation.user.dto.KakaoUserInfoDto;
import com.dnd12.meetinginvitation.user.dto.LoginResponse;
import com.dnd12.meetinginvitation.user.entity.User;
import com.dnd12.meetinginvitation.user.repository.UserRepository;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.client-secret}")
    private String clientSecret;

    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    public LoginResponse handleKakaoLogin(String code) {
        // 카카오 액세스 토큰 받기
        KakaoTokenResponseDto tokenResponse = getAccessTokenFromKakao(code);

        // 카카오 사용자 정보 받기
        KakaoUserInfoDto userInfo = getUserInfo(tokenResponse.getAccessToken());

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
                    .profileImage(userInfo.kakaoAccount.profile.getProfileImageUrl())
//                    .profileImage(userInfo.kakaoAccount.profile.getThumbnailImageUrl())
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
        }
        //JWT 토큰 생성

        String accessToken = jwtTokenProvider.createToken(user.getEmail());

        //응답 생성
        return LoginResponse.builder()
                .accessToken(accessToken)
                .email(user.getEmail())
                .name(user.getName())
                .build();

    }

    public KakaoTokenResponseDto getAccessTokenFromKakao(String code) {
        log.info(" [Kakao clientId] Access Token ------> {}", clientId);
        log.info(" [Kakao clientSecret] Access Token ------> {}", clientSecret);
        log.info(" [Kakao code] Access Token ------> {}", code);

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
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
//                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();


        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());

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

        log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
        log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }
}
