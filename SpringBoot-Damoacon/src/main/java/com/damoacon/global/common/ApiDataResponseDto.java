package com.damoacon.global.common;

import com.damoacon.global.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ApiDataResponseDto<T> extends ApiResponseDto {
    private final T data;

    private ApiDataResponseDto(T data) {
        super(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }

    private ApiDataResponseDto(T data, String message) {
        super(true, ErrorCode.OK.getCode(), message);
        this.data = data;
    }

    public static <T> ApiDataResponseDto<T> of(T data) {
        return new ApiDataResponseDto<>(data);
    }

    public static <T> ApiDataResponseDto<T> of(T data, String message) {
        return new ApiDataResponseDto<>(data, message);
    }

    public static <T> ApiDataResponseDto<T> empty() {
        return new ApiDataResponseDto<>(null);
    }
}
