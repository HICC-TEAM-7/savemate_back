package hicc.budget.demo.Notification.exception;

import lombok.Getter;

@Getter
public class NotificationException extends RuntimeException {
    private final NotificationExceptionType errorType;

    public NotificationException(NotificationExceptionType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
