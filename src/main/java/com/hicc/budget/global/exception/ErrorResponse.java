package com.hicc.budget.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String code;
    private final String message;
    private final String path;

    // Constructor for direct field injection with ErrorCode
    public ErrorResponse(ErrorCode errorCode, String message, String path) {
        this(errorCode.getStatus(), errorCode.getCode(), message, path);
    }
    
    // Constructor for direct field injection with all fields
    public ErrorResponse(int status, String code, String message, String path) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    // Builder-based factory methods
    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return new ErrorResponse(errorCode, errorCode.getMessage(), path);
    }

    public static ErrorResponse of(ErrorCode errorCode, String message, String path) {
        return new ErrorResponse(errorCode, message, path);
    }
}
