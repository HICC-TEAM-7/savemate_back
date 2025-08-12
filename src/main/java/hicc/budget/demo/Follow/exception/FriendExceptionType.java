package hicc.budget.demo.Follow.exception;

import lombok.Getter;

@Getter
public enum FriendExceptionType {
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    REQUEST_NOT_FOUND("요청을 찾을 수 없습니다."),
    ALREADY_FRIENDS("이미 친구인 사용자입니다."),
    ALREADY_REQUESTED("이미 친구 요청을 보냈습니다."),
    CANNOT_REQUEST_TO_SELF("자기 자신에게 친구 요청을 보낼 수 없습니다."),
    NOT_REQUEST_RECEIVER("요청을 수락할 권한이 없습니다."),
    INVALID_REQUEST_STATUS("유효하지 않은 요청 상태입니다.");

    private final String message;

    FriendExceptionType(String message) {
        this.message = message;
    }
}
