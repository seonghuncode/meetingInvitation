package com.dnd12.meetinginvitation.invitation.repository;

import com.dnd12.meetinginvitation.invitation.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Optional<Theme> findByThemeName(String themeName);
}

