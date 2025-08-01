package com.hicc.budget.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.of(errorCode, request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        
        ErrorResponse response = ErrorResponse.builder()
                .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                .code(ErrorCode.INVALID_INPUT_VALUE.getCode())
                .message(errorMessage)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.valueOf(ErrorCode.INVALID_INPUT_VALUE.getStatus())
        );

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.of(
                ErrorCode.INTERNAL_SERVER_ERROR, 
                e.getMessage(), 
                request.getRequestURI()
        );
        return new ResponseEntity<>(
                response,
                HttpStatus.valueOf(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
        );

    }
}
