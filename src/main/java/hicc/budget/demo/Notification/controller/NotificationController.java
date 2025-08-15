package hicc.budget.demo.Notification.controller;

import hicc.budget.demo.Notification.dto.NotificationResponse;
import hicc.budget.demo.Notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // [변경] @LoginUser 제거, 쿼리 파라미터로 userId 받음
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @RequestParam Long userId // 로그인 제거했으니 명시적으로 받기
    ) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    // [변경] 동일하게 userId를 쿼리 파라미터로
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    // [변경] 카운트도 userId 파라미터
    @GetMapping("/unread/count")
    public ResponseEntity<Integer> getUnreadCount(
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    // [변경] 읽음 처리: path의 알림 id + 쿼리 파라미터 userId
    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long id,
            @RequestParam Long userId // 소유자 검증은 서비스에서 수행
    ) {
        notificationService.markAsRead(id, userId);
        return ResponseEntity.ok().build();
    }

    // [변경] 모두 읽음: userId 파라미터
    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @RequestParam Long userId
    ) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}
