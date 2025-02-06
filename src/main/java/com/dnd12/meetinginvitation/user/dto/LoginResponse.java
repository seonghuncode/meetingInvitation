package com.dnd12.meetinginvitation.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    //    private boolean isNewUser; //신규 회원인지
    private String email;
    private String name;
}
