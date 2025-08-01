package com.hicc.budget.repository;

import com.hicc.budget.domain.notification.entity.Notification;
import com.hicc.budget.domain.notification.entity.NotificationType;
import com.hicc.budget.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // 사용자의 읽지 않은 알림 목록 조회 (최신순)
    Page<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // 사용자의 모든 알림 목록 조회 (최신순)
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // 특정 유형의 알림 목록 조회
    List<Notification> findByUserIdAndType(Long userId, NotificationType type);
    
    // 알림을 읽음으로 표시
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.id = :id")
    void markAsRead(@Param("id") Long id, @Param("readAt") LocalDateTime readAt);
    
    // 여러 알림을 한 번에 읽음으로 표시
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.id IN :ids")
    void markAllAsRead(@Param("ids") List<Long> ids, @Param("readAt") LocalDateTime readAt);
    
    // 사용자의 모든 알림을 읽음으로 표시
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.user.id = :userId AND n.isRead = false")
    int markAllAsReadByUserId(@Param("userId") Long userId, @Param("readAt") LocalDateTime readAt);
    
    // 읽지 않은 알림 수 조회
    long countByUserIdAndIsReadFalse(Long userId);
}
