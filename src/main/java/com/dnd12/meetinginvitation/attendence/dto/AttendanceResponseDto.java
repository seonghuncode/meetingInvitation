package com.dnd12.meetinginvitation.attendence.dto;

import com.dnd12.meetinginvitation.invitation.enums.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceResponseDto {
    private Long invitationId;
    private Long userId;
    private String name;
    private AttendanceStatus state;
    private LocalDateTime writeDate;
}
