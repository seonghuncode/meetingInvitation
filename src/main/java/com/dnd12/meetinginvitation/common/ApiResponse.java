package com.dnd12.meetinginvitation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    // 성공 응답 생성
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    // 성공 응답 생성 (메시지 포함)
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }

    // 성공 응답 생성 (메시지만)
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, null, message);
    }

    // 에러 응답 생성
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }

    // 에러 응답 생성 (데이터 포함)
    public static <T> ApiResponse<T> error(T data, String message) {
        return new ApiResponse<>(false, data, message);
    }
}
