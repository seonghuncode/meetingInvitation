package com.dnd12.meetinginvitation.invitation.dto;


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
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String place;
    private String detail_address;
    private LocalDateTime date;
    private int max_attendances;
    private String description;
    private String state;
    private String link;
    //private MultipartFile invitationTemplate;
    private String imageData;
    private String invitationTemplate_url;
    private String invitationType;

}
