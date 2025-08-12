package hicc.budget.demo.Notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hicc.budget.demo.Notification.domain.Notification;
import hicc.budget.demo.Notification.domain.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private String relatedEntityId;
    private Long senderId;
    private String senderName;
    private Long missionId;
    private String missionTitle;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.isRead())
                .relatedEntityId(notification.getRelatedEntityId())
                .senderId(notification.getSender() != null ? notification.getSender().getId() : null)
                .senderName(notification.getSender() != null ? 
                    (notification.getSender().getNickname() != null ? 
                        notification.getSender().getNickname() : 
                        notification.getSender().getName()) : 
                    null)
                .missionId(notification.getMission() != null ? notification.getMission().getId() : null)
                .missionTitle(notification.getMission() != null ? notification.getMission().getTitle() : null)
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
