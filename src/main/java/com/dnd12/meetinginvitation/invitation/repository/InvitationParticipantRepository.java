package com.dnd12.meetinginvitation.invitation.repository;

import com.dnd12.meetinginvitation.invitation.entity.InvitationParticipant;
import com.dnd12.meetinginvitation.invitation.enums.InvitationType;
import com.dnd12.meetinginvitation.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationParticipantRepository extends JpaRepository<InvitationParticipant, Long> {

    // 특정 사용자에 대한 타입별 목록 조회 (CREATOR/INVITED)
    List<InvitationParticipant> findByUserAndInvitationType(User user, InvitationType invitationType);
    InvitationParticipant findByInvitationIdAndUserId(Long invitationId, Long userId);
}
