package com.damoacon.global.common;

import com.damoacon.global.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponseDto {
    private final Integer code;
    private final String message;

    public static ApiResponseDto of(Integer code) {
        return new ApiResponseDto(code, HttpStatus.valueOf(code).getReasonPhrase());
    }

    public static ApiResponseDto of(Integer code, String message) {
        return new ApiResponseDto(code, message);
    }

    public static ApiResponseDto of(ErrorCode errorCode, String message) {
        return new ApiResponseDto(errorCode.getCode(), errorCode.getMessage(message));
    }
}
