package com.dnd12.meetinginvitation.invitation.entity;

import com.dnd12.meetinginvitation.attendence.entity.Attendance;
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

    @OneToMany(mappedBy = "invitation", cascade = CascadeType.REMOVE)
    private List<Attendance> attendances = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
    private String organizerName;
    private String place;
    private String detailAddress;
    private LocalDateTime date;

    @Column(nullable = false)
    private int maxAttendences;

    private String description;

    @Column(nullable = false)
    private String state;
     
    private String link;

    private String title;

    // 초대장은 하나의 폰트를 사용
    @ManyToOne
    @JoinColumn(name = "font_id", nullable = true)
    private Font font;

    // 초대장은 하나의 스티커를 사용
    @ManyToOne
    @JoinColumn(name = "sticker_id", nullable = true)
    private Sticker sticker;

//    @OneToOne
//    @JoinColumn(name = "template_id", nullable = false)
//    private Background background;

    // 편지지 종류
    @ManyToOne
    @JoinColumn(name = "theme_id", nullable = true)
    private Theme theme;

    private String backgroundUrl;

    private String basicBackgroundType;
}