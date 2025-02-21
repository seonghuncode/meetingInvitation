package com.dnd12.meetinginvitation.attendence.dto;

import com.dnd12.meetinginvitation.invitation.enums.AttendanceStatus;
import lombok.Getter;

@Getter
public class AttendanceRequest {

    private Long invitationId;
    private AttendanceStatus state;
    private String name;
    private String password;
    private String message;
}
