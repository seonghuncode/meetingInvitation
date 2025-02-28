package com.dnd12.meetinginvitation.attendence.entity;

import com.dnd12.meetinginvitation.invitation.entity.Invitation;
import com.dnd12.meetinginvitation.invitation.enums.AttendanceStatus;
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
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invitation_id", nullable = false)
    private Invitation invitation;

    // 참석한 사용자 (ManyToOne 관계)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //ATTENDING(참석), NOT_ATTENDING(불참), PENDING(보류(미정))
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttendanceStatus state; // --> 참석 여부 enum type

    @Column(nullable = false)
    private String name;

//    @Column(nullable = false)
    private String password;

    @ElementCollection
    private List<String> messages = new ArrayList<>();

    //응답 날짜
    private LocalDateTime date;

    @Builder
    public Attendance(Invitation invitation, User user, AttendanceStatus state, String name, String password, LocalDateTime date, String message) {

        this.invitation = invitation;
        this.user = user;
        this.state = state;
        this.name = name;
        this.password = password;
        this.date = date;
        if (message != null && !message.trim().isEmpty()) {
            this.messages.add(message);
        }
    }

    // 메시지 추가 메서드
    public void addMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            this.messages.add(message);
        }
    }
}