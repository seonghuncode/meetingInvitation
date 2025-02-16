package com.dnd12.meetinginvitation.attendence.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class AttendanceRequest {

    private Long invitationId;
    private String state;
    private String name;
    private String password;
    private String message;
}
