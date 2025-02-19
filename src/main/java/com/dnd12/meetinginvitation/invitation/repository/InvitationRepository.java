package com.dnd12.meetinginvitation.invitation.repository;


import com.dnd12.meetinginvitation.invitation.entity.Invitation;
import com.dnd12.meetinginvitation.invitation.enums.InvitationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByUserId(Long userId);
    Optional<Invitation> findById(Long id);
    @Query("SELECT i FROM Invitation i WHERE i.id = :id")
    Invitation findInvitationById(@Param("id") Long id);
    Page<Invitation> findByUserId(Long userId, Pageable pageable);
    Page<Invitation> findByParticipantsUserIdAndParticipantsInvitationType(Long userId, InvitationType invitationType, Pageable pageable);
}
