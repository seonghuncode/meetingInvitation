package com.dnd12.meetinginvitation.invitation.dto;


import com.dnd12.meetinginvitation.invitation.enums.InvitationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationDto {
    private Long creator_id;
    private Long invitationId;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String place;
    private String detail_address;
    private LocalDateTime date;
    private int max_attendances;
    private String description;
    private String state;
    private String link;
    private InvitationType invitationType;
    private String fontName;
    private String sticker;
    private String title;
    private String backgroundImageData;
    private String organizerName;
    private String themeName;


    public InvitationDto(Long id, Long id1, LocalDateTime createdAt, LocalDateTime updatedAt, String organizerName, String place, String detailAddress, LocalDateTime date, int maxAttendences, String description, String state, String link, String title, String fontName, String stickerName, String backgroundUrl, String themeName) {
    }
}



