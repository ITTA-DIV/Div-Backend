package com.damoacon.domain.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ResponseDto<T> {

    private HttpStatus status;
    private String message;
    private T data;

    public ResponseDto(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public static<T> ResponseDto<T> response(HttpStatus status, String message) {
        return response(status, message, null);
    }

    public static<T> ResponseDto<T> response(HttpStatus status, String message, T t) {
        return ResponseDto.<T>builder()
                .status(status)
                .message(message)
                .data(t)
                .build();
    }
}