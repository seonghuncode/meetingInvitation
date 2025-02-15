package com.dnd12.meetinginvitation.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
//    private String refreshToken; => 보류
    private String email;
    private String name;
    private Long userId;
    private String profileImageUrl;
}
