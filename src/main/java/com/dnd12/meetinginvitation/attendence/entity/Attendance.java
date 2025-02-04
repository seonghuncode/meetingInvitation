package com.dnd12.meetinginvitation.attendence.entity;

import com.dnd12.meetinginvitation.invitation.entity.Invitation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invitation_id", nullable = false)
    private Invitation invitation;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @ElementCollection
    private java.util.List<String> messages;
}