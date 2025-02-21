package com.dnd12.meetinginvitation.attendence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AttendanceResponseDto {
    private Long invitationId;
    private Long userId;
    private String name;
    private String state;
    private LocalDateTime writeDate;
    private List<String> messages;
}
