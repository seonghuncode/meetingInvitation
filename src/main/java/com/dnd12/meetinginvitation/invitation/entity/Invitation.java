package com.dnd12.meetinginvitation.invitation.entity;

import com.dnd12.meetinginvitation.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "invitation", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<InvitationParticipant> participants = new ArrayList<>();

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

    private String invitationTemplate_url;


}