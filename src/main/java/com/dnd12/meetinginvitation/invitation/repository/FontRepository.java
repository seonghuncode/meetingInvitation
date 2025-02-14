package com.dnd12.meetinginvitation.invitation.repository;

import com.dnd12.meetinginvitation.invitation.entity.Font;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FontRepository extends JpaRepository<Font, Long>{
    // 폰트 이름으로 폰트를 조회하는 메서드
    Optional<Font> findByFontName(String fontName);

}



