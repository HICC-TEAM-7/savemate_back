package com.hicc.budget.domain.user.entity;

import com.hicc.budget.domain.expense.entity.Expense;
import com.hicc.budget.domain.friend.entity.Friend;
import com.hicc.budget.domain.mission.entity.UserMission;
import com.hicc.budget.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String nickname;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType; // KAKAO, GOOGLE, APPLE, EMAIL

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    private String refreshToken; // 리프레시 토큰

    @Column(nullable = false)
    private boolean notificationEnabled = true; // 알림 수신 여부

    // 연관 관계 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMission> userMissions = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> following = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> followers = new ArrayList<>();

    @Builder
    public User(String email, String password, String nickname, String profileImageUrl, 
               UserRole role, SocialType socialType, String socialId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role != null ? role : UserRole.USER;
        this.socialType = socialType != null ? socialType : SocialType.EMAIL;
        this.socialId = socialId;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(String password) {
        this.password = password;
    }

    // 닉네임 업데이트
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 프로필 이미지 업데이트
    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    // 알림 설정 업데이트
    public void updateNotificationSetting(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    // 리프레시 토큰 업데이트
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // 권한 설정 메소드
    public void authorizeUser() {
        this.role = UserRole.USER;
    }
}
