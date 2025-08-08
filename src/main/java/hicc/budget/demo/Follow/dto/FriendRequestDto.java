package hicc.budget.demo.Follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FriendRequestDto {
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendRequest {
        private Long receiverId;
    }
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcceptRequest {
        private Long requestId;
    }
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestResponse {
        private Long id;
        private Long senderId;
        private String senderUsername;
        private String senderName;
    }
}
