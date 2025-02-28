package com.dnd12.meetinginvitation.invitation.entity;

import com.dnd12.meetinginvitation.invitation.enums.InvitationType;
import com.dnd12.meetinginvitation.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "invitation_participant")
public class InvitationParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invitation_id", nullable = false)
    private Invitation invitation; // 초대장 정보

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 초대장에 대한 사용자 정보

    @Enumerated(EnumType.STRING)
    private InvitationType invitationType; // 초대 유형 (CREATOR, INVITED)

    //초대장 날짜(생성된 or 초대받은)
    private LocalDateTime date;

}