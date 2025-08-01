package com.hicc.budget.domain.notification.entity;

import com.hicc.budget.domain.user.entity.User;
import com.hicc.budget.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 알림 수신자

    @Column(nullable = false)
    private String title; // 알림 제목

    @Column(columnDefinition = "TEXT")
    private String content; // 알림 내용

    @Column(nullable = false)
    private String relatedUrl; // 관련 링크 (클릭 시 이동할 URL)

    @Column(nullable = false)
    private boolean isRead = false; // 읽음 여부

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type; // 알림 유형

    private LocalDateTime readAt; // 읽은 시간

    @Builder
    public Notification(User user, String title, String content, 
                       String relatedUrl, NotificationType type) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.relatedUrl = relatedUrl;
        this.type = type != null ? type : NotificationType.ETC;
    }

    // 알림을 읽음으로 표시
    public void markAsRead() {
        if (!this.isRead) {
            this.isRead = true;
            this.readAt = LocalDateTime.now();
        }
    }
}
