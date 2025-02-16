package com.dnd12.meetinginvitation.attendence.dto;

import lombok.Getter;

@Getter
public class UserAttendanceRequest {
    private Long invitationId;
    private Long userId;
    private String state;
    private String name;
    private String message;
}
