package com.damoacon.global.common;

import com.damoacon.global.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponseDto {
    private final Boolean success;
    private final Integer code;
    private final String message;

    public static ApiResponseDto of(Boolean success, ErrorCode code) {
        return new ApiResponseDto(success, code.getCode(), code.getMessage());
    }

    public static ApiResponseDto of(Boolean success, ErrorCode errorCode, Exception e) {
        return new ApiResponseDto(success, errorCode.getCode(), errorCode.getMessage(e));
    }

    public static ApiResponseDto of(Boolean success, ErrorCode errorCode, String message) {
        return new ApiResponseDto(success, errorCode.getCode(), errorCode.getMessage(message));
    }
}
