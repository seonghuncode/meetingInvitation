package com.dnd12.meetinginvitation.invitation.repository;


import com.dnd12.meetinginvitation.invitation.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByUserId(Long userId);

    Invitation findByid(Long id);
}
