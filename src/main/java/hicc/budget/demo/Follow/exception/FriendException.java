package hicc.budget.demo.Follow.exception;

import lombok.Getter;

@Getter
public class FriendException extends RuntimeException {
    private final FriendExceptionType exceptionType;

    public FriendException(FriendExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }
}
