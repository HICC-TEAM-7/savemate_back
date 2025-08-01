package com.hicc.budget.domain.friend.entity;

import com.hicc.budget.domain.user.entity.User;
import com.hicc.budget.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "follow_uk",
        columnNames = {"follower_id", "following_id"}
    )
})
public class Friend extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower; // 팔로우를 요청한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following; // 팔로우 대상 사용자

    @Column(nullable = false)
    private boolean isNotificationEnabled = true; // 알림 수신 여부

    @Column(nullable = false)
    private boolean isBlocked = false; // 차단 여부

    @Builder
    public Friend(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    // 알림 설정 토글
    public void toggleNotification() {
        this.isNotificationEnabled = !this.isNotificationEnabled;
    }

    // 차단 설정 토글
    public void toggleBlock() {
        this.isBlocked = !this.isBlocked;
        // 차단 시 알림도 함께 끄기
        if (this.isBlocked) {
            this.isNotificationEnabled = false;
        }
    }
}
