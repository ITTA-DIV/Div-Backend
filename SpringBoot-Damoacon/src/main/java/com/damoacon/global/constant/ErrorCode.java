package com.damoacon.global.constant;

import com.damoacon.global.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    OK(0, HttpStatus.OK, "Ok"),

    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "Bad request"),
    INVALID_ACCESS(10001, HttpStatus.BAD_REQUEST, "Invalid access"),
    VALIDATION_ERROR(10002, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(10003, HttpStatus.NOT_FOUND, "Requested resource is not found"),

    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),

    AUTHENTICATION_FAILED(40000, HttpStatus.UNAUTHORIZED, "User unauthorized"),
    AUTHORIZATION_FAILED(40001, HttpStatus.UNAUTHORIZED, "Authorization failed."),
    ACCESS_TOKEN_REQUIRED(40002, HttpStatus.UNAUTHORIZED, "Access Token required."),
    REFRESH_TOKEN_REQUIRED(40003, HttpStatus.UNAUTHORIZED, "Refresh Token required."),
    INVALID_TOKEN(40004, HttpStatus.UNAUTHORIZED, "Invalid token."),
    ACCESS_TOKEN_EXPIRED(40004, HttpStatus.UNAUTHORIZED, "Access Token expired."),
    LOGIN_REQUIRED(40005, HttpStatus.FORBIDDEN, "Login required.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    public static ErrorCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return ErrorCode.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return ErrorCode.INTERNAL_ERROR;
                    } else {
                        return ErrorCode.OK;
                    }
                });
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}
