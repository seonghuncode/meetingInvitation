package com.dnd12.meetinginvitation.user.controller;

import com.dnd12.meetinginvitation.common.ApiResponse;
import com.dnd12.meetinginvitation.user.dto.GetUserDto;
import com.dnd12.meetinginvitation.user.entity.User;
import com.dnd12.meetinginvitation.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUser(userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("회원탈퇴 처리 중 오류가 발생했습니다."));
        }
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴 성공했습니다."));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<GetUserDto>> userDetails(@PathVariable("userId") Long userId) {
        try {
            Optional<GetUserDto> userInfo = userService.getUser(userId);
            if (userInfo.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("회원이 존재하지 않습니다."));
            }
            GetUserDto getUser = userInfo.get();
            return ResponseEntity.ok(ApiResponse.success(getUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("회원 조회 중 오류가 발생했습니다."));
        }


    }
}
