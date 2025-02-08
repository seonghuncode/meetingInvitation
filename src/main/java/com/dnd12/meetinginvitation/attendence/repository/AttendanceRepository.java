package com.dnd12.meetinginvitation.attendence.repository;

import com.dnd12.meetinginvitation.attendence.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByInvitationId(Long invitationId);
}
