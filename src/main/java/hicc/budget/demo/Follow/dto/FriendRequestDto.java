package hicc.budget.demo.Follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FriendRequestDto {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendRequest {
        private Long receiverId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcceptRequest {
        private Long requestId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RequestResponse {
        private Long id;
        private Long senderId;
        private String senderNickname;
        private String senderEmail;
    }
}
