package com.dnd12.meetinginvitation.invitation.repository;

import com.dnd12.meetinginvitation.invitation.entity.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  StickerRepository  extends JpaRepository<Sticker, Long> {
    Optional<Sticker> findByStickerName(String stickerName);
}
