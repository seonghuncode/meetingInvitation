package com.dnd12.meetinginvitation.attendence.service;

import com.dnd12.meetinginvitation.attendence.dto.AttendanceRequest;
import com.dnd12.meetinginvitation.attendence.dto.UserAttendanceRequest;
import com.dnd12.meetinginvitation.attendence.entity.Attendance;
import com.dnd12.meetinginvitation.attendence.repository.AttendanceRepository;
import com.dnd12.meetinginvitation.invitation.entity.Invitation;
import com.dnd12.meetinginvitation.invitation.entity.InvitationParticipant;
import com.dnd12.meetinginvitation.invitation.enums.InvitationType;
import com.dnd12.meetinginvitation.invitation.repository.InvitationParticipantRepository;
import com.dnd12.meetinginvitation.invitation.repository.InvitationRepository;
import com.dnd12.meetinginvitation.user.entity.User;
import com.dnd12.meetinginvitation.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final PasswordEncoder passwordEncoder;
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final InvitationParticipantRepository invitationParticipantRepository;


    //비회원 응답
    public void saveAttendance(AttendanceRequest request) {
        Invitation invitation = invitationRepository.findById(request.getInvitationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid invitation ID"));

        boolean check = attendanceRepository.existsByName(request.getName());

        if(check){
            throw new RuntimeException("Fail: You have already responded. [responded Name :  " + request.getName() + "]");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Attendance attendance = Attendance.builder()
                .invitation(invitation)
                .state(request.getState())
                .name(request.getName())
                .password(encodedPassword)  // 암호화된 비밀번호 저장
                .message(request.getMessage())
                .date(LocalDateTime.now())
                .build();

        attendanceRepository.save(attendance);
    }

    //회원 응답
    public void saveUserAttendance(UserAttendanceRequest userAttendanceRequest) {
        log.info("invitationId:{}", userAttendanceRequest.getInvitationId());

        Invitation invitation = invitationRepository.findById(userAttendanceRequest.getInvitationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid invitation ID"));

        User user = userRepository.findById(userAttendanceRequest.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("invalid user ID"));

        boolean check = attendanceRepository.existsByUser_Id(userAttendanceRequest.getUserId());

        if(check){
            throw new RuntimeException("Fail: You have already responded. [responded Name :  " + userAttendanceRequest.getUserId() + "]");
        }

        Attendance attendance = Attendance.builder()
                .invitation(invitation)
                .user(user)
                .state(userAttendanceRequest.getState())
                .name(userAttendanceRequest.getName())
                .message(userAttendanceRequest.getMessage())
                .date(LocalDateTime.now())
                .build();

        //invitation_participant 테이블에 저장
        InvitationParticipant invitationParticipant = InvitationParticipant.builder()
                .invitation(invitation)
                .user(user)
                .invitationType(InvitationType.INVITED)
                .date(LocalDateTime.now())
                .build();

        invitationParticipantRepository.save(invitationParticipant);
        attendanceRepository.save(attendance);


    }


    // 비밀번호 확인 메서드
    public boolean checkPassword(Long attendanceId, String rawPassword) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attendance ID"));

        return passwordEncoder.matches(rawPassword, attendance.getPassword());
    }
}
