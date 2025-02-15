package com.dnd12.meetinginvitation.attendence.entity;

import com.dnd12.meetinginvitation.invitation.entity.Invitation;
import com.dnd12.meetinginvitation.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @ElementCollection
    private List<String> messages = new ArrayList<>();

    @Builder
    public Attendance(Invitation invitation, String state, String name, String password, String message) {

        this.invitation = invitation;
        this.state = state;
        this.name = name;
        this.password = password;
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