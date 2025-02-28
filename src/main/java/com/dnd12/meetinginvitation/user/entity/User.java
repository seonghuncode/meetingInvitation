package com.dnd12.meetinginvitation.user.entity;

import com.dnd12.meetinginvitation.attendence.entity.Attendance;
import com.dnd12.meetinginvitation.invitation.entity.Invitation;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users") // 'user'는 예약어이므로 'users'로 지정
public class User {

    @Id
    private Long Id; //kakaoId

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

//    @Lob
    private String profileImageUrl;

//    private String thumbnailImage;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Invitation> createdInvitations;
}
