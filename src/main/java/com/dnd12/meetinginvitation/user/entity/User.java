package com.dnd12.meetinginvitation.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users") // 'user'는 예약어이므로 'users'로 지정
public class User {

    @Id
    private Long Id; //kakaoId

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

//    @Lob
    private String profileImage;

//    private String thumbnailImage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
