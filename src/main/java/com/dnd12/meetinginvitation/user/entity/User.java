package com.dnd12.meetinginvitation.user.entity;

import com.dnd12.meetinginvitation.attendence.entity.Attendance;
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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 초대장의 참석 여부 목록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();
}
