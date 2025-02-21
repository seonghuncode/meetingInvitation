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
    public InvitationDto(Long creatorId, Long invitationId, LocalDateTime createdAt, LocalDateTime updatedAt, String organizerName, String place, String detailAddress, LocalDateTime date, int maxAttendances, String description, String state, String link, InvitationType invitationType, String fontName, String sticker, String title, String backgroundImageData, String themeName) {
        this.creatorId = creatorId;
        this.invitationId = invitationId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.organizerName = organizerName;
        this.place = place;
        this.detailAddress = detailAddress;
        this.date = date;
        this.maxAttendances = maxAttendances;
        this.description = description;
        this.state = state;
        this.link = link;
        this.invitationType = invitationType;
        this.fontName = fontName;
        this.sticker = sticker;
        this.title = title;
        this.backgroundImageData = backgroundImageData;
        this.themeName = themeName;
    }

    private Long creatorId;
    private Long invitationId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String place;
    private String detailAddress;
    private LocalDateTime date;
    private int maxAttendances;
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
    private String basicBackgroundType;



//    public InvitationDto(Long id, Long id1, LocalDateTime createdAt, LocalDateTime updatedAt, String organizerName, String place, String detailAddress, LocalDateTime date, int maxAttendences, String description, String state, String link, String title, String fontName, String stickerName, String backgroundUrl, String themeName) {
//    }




}



