package hicc.budget.demo.Notification.exception;

import lombok.Getter;

@Getter
public enum NotificationExceptionType {
    NOTIFICATION_NOT_FOUND("알림을 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS("해당 알림에 접근할 권한이 없습니다."),
    INVALID_INPUT("잘못된 입력값입니다.");

    private final String message;

    NotificationExceptionType(String message) {
        this.message = message;
    }
}
