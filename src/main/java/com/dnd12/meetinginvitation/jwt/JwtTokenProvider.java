package com.dnd12.meetinginvitation.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7;
    //private final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60 * 3; // 액세스 토큰 유효시간 3시간
    private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24; //리프레시 토큰 유효시간

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 토큰의 남은 유효시간을 밀리초 단위로 반환
    public Long getExpirationTime(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            Date now = new Date();

            return expiration.getTime() - now.getTime();
        } catch (Exception e) {
            log.error("Failed to get token expiration time", e);
            return 0L;
        }
    }

    // 토큰의 만료 시간을 Date 객체로 반환
    public Date getExpirationDate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // Access 토큰 생성
    public String createAccessToken(String userEmail) {
        log.info("createAccessToken");
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("type", "access");

        return createToken(claims, ACCESS_TOKEN_VALIDITY);
    }

    // Refresh 토큰 생성
    public String createRefreshToken(String userEmail) {
        log.info("createRefreshToken");
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("type", "refresh");

        return createToken(claims, REFRESH_TOKEN_VALIDITY);
    }

    public String createToken(Claims claims, long validitytime) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validitytime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSigningKey())
                .compact();
    }

    public void saveRefreshToken(String email, String refreshToken) {
    }
    // 토큰에서 이메일 추출
    public String getUserEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //secretKey를 byte 배열로 변환
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
