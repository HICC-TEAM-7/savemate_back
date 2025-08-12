package hicc.budget.demo.Follow.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FollowExceptionHandler {

    @ExceptionHandler(FriendException.class)
    public ResponseEntity<ErrorResponse> handleFriendException(FriendException e) {
        ErrorResponse response = new ErrorResponse(e.getExceptionType().name(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorResponse {
        private String code;
        private String message;
    }
}
