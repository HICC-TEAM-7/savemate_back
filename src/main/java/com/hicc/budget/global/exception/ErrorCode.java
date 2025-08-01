package com.hicc.budget.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(400, "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    
    // User
    USER_NOT_FOUND(400, "U001", "User not found"),
    INVALID_PASSWORD(400, "U002", "Invalid password"),
    EMAIL_DUPLICATION(400, "U003", "Email is duplicated"),
    EMAIL_ALREADY_EXISTS(400, "U004", "Email already exists"),
    NICKNAME_ALREADY_EXISTS(400, "U005", "Nickname already exists"),
    
    // Auth
    UNAUTHORIZED(401, "A001", "Unauthorized"),
    INVALID_TOKEN(401, "A002", "Invalid token"),
    INVALID_REFRESH_TOKEN(401, "A003", "Invalid refresh token"),
    TOKEN_EXPIRED(401, "A004", "Token expired"),
    ACCESS_DENIED(403, "A005", "Access denied"),
    
    // JWT
    JWT_INVALID(401, "J001", "JWT token is invalid"),
    JWT_EXPIRED(401, "J002", "JWT token is expired"),
    JWT_UNSUPPORTED(401, "J003", "JWT token is unsupported"),
    JWT_EMPTY(401, "J004", "JWT claims string is empty");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
