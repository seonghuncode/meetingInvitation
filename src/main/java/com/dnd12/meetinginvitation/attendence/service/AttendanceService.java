package com.dnd12.meetinginvitation.attendence.service;

import com.dnd12.meetinginvitation.attendence.dto.AttendanceRequest;
import com.dnd12.meetinginvitation.attendence.entity.Attendance;
import com.dnd12.meetinginvitation.attendence.repository.AttendanceRepository;
import com.dnd12.meetinginvitation.invitation.entity.Invitation;
import com.dnd12.meetinginvitation.invitation.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final PasswordEncoder passwordEncoder;
    private final InvitationRepository invitationRepository;

    public Attendance saveAttendance(AttendanceRequest request) {
        log.info("invitationId:{}", request.getInvitationId());
        Invitation invitation = invitationRepository.findById(request.getInvitationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid invitation ID"));

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Attendance attendance = Attendance.builder()
                .invitation(invitation)
                .state(request.getState())
                .name(request.getName())
                .password(encodedPassword)  // 암호화된 비밀번호 저장
                .message(request.getMessage())
                .build();

        return attendanceRepository.save(attendance);
    }

    // 비밀번호 확인 메서드
    public boolean checkPassword(Long attendanceId, String rawPassword) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attendance ID"));

        return passwordEncoder.matches(rawPassword, attendance.getPassword());
    }
}
