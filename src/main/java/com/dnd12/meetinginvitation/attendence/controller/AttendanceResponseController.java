package com.dnd12.meetinginvitation.attendence.controller;

import com.dnd12.meetinginvitation.attendence.dto.AttendanceRequest;
import com.dnd12.meetinginvitation.attendence.entity.Attendance;
import com.dnd12.meetinginvitation.attendence.service.AttendanceService;
import com.dnd12.meetinginvitation.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.batch.BatchTaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attendance")
@Slf4j
public class AttendanceResponseController {

    private final AttendanceService attendanceService;

    @PostMapping("/response")
    public ResponseEntity<ApiResponse<Attendance>> AttendanceResponse(@RequestBody AttendanceRequest request) {
        log.info("invitation_id:{}",request.getInvitationId());
        log.info("name:{}",request.getName());
        log.info("password:{}",request.getPassword());
        log.info("state:{}",request.getState());
        Attendance attendance = attendanceService.saveAttendance(request);

        return ResponseEntity.ok(ApiResponse.success(attendance));
    }
}
