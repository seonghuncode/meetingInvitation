package com.dnd12.meetinginvitation.domain.invitation.entity;

import com.dnd12.meetinginvitation.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "invitation")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private String place;
    private String detailAddress;
    private LocalDateTime date;

    @Column(nullable = false)
    private int maxAttendences;

    private String description;

    @Column(nullable = false)
    private String state;

    private String link;

    @Lob
    private byte[] invitationTemplate;

    private String invitationType;
}