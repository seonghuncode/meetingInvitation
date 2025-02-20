package com.dnd12.meetinginvitation.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetUserDto {
    private Long userId;
    private String name;
    private String profileImage;
}
