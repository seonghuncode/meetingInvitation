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


    public InvitationDto(Long creator_id, Long invitationId, LocalDateTime created_at,
                               LocalDateTime updated_at, String place, String detail_address,
                               LocalDateTime date, int max_attendances, String description,
                               String state, String link, String fontName, String sticker, String title, String backgroundImageData) {
        this.creator_id = creator_id;
        this.invitationId = invitationId;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.place = place;
        this.detail_address = detail_address;
        this.date = date;
        this.max_attendances = max_attendances;
        this.description = description;
        this.state = state;
        this.link = link;
        this.fontName = fontName;
        this.sticker = sticker;
        this.title = title;
        this.backgroundImageData = backgroundImageData;
    }


}



