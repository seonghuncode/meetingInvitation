package com.dnd12.meetinginvitation.attendence.dto;

import com.dnd12.meetinginvitation.invitation.enums.AttendanceStatus;
import lombok.Getter;

@Getter
public class UserAttendanceRequest {
    private Long invitationId;
    private Long userId;
    private AttendanceStatus state;
    private String name;
    private String message;
}
