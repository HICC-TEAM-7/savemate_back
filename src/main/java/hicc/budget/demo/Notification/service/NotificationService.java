package hicc.budget.demo.Notification.service;

import hicc.budget.demo.Mission.domain.Mission;
import hicc.budget.demo.Notification.domain.Notification;
import hicc.budget.demo.Notification.domain.NotificationType;
import hicc.budget.demo.Notification.domain.RelatedEntityType;
import hicc.budget.demo.Notification.dto.NotificationResponse;
import hicc.budget.demo.Notification.exception.NotificationException;
import hicc.budget.demo.Notification.exception.NotificationExceptionType;
import hicc.budget.demo.Notification.repository.NotificationRepository;
import hicc.budget.demo.User.domain.User;
import hicc.budget.demo.User.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId).stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(NotificationExceptionType.NOTIFICATION_NOT_FOUND));
        
        if (!notification.getUser().getId().equals(userId)) {
            throw new NotificationException(NotificationExceptionType.UNAUTHORIZED_ACCESS);
        }
        
        notification.markAsRead();
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        unreadNotifications.forEach(Notification::markAsRead);
        notificationRepository.saveAll(unreadNotifications);
    }
    
    @Transactional(readOnly = true)
    public int getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public Notification createFriendRequestNotification(User receiver, User sender) {
        Notification notification = Notification.builder()
                .user(receiver)
                .sender(sender)
                .title("새로운 친구 요청")
                .message(sender.getNickname() + "님이 친구 요청을 보냈습니다.")
                .type(NotificationType.FRIEND_REQUEST_RECEIVED)
                .relatedEntityType(RelatedEntityType.FRIEND_REQUEST)
                .build();
        
        return createNotification(notification);
    }

    @Transactional
    public Notification createFriendRequestAcceptedNotification(User receiver, User sender) {
        Notification notification = Notification.builder()
                .user(receiver)
                .sender(sender)
                .title("친구 요청 수락")
                .message(sender.getNickname() + "님이 친구 요청을 수락하셨습니다.")
                .type(NotificationType.FRIEND_REQUEST_ACCEPTED)
                .relatedEntityType(RelatedEntityType.FRIEND_REQUEST)
                .build();
        
        return createNotification(notification);
    }

    @Transactional
    public Notification createMissionProgressNotification(User user, Mission mission, double progress) {
        Notification notification = Notification.builder()
                .user(user)
                .mission(mission)
                .title("미션 진행 상황")
                .message(String.format("%s 미션 달성률이 %.1f%%가 되었습니다.", mission.getTitle(), progress))
                .type(NotificationType.MISSION_PROGRESS)
                .relatedEntityType(RelatedEntityType.USER_MISSION)
                .build();
        
        return createNotification(notification);
    }
}
