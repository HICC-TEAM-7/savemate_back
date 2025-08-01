package com.hicc.budget.domain.mission.entity;

import com.hicc.budget.domain.user.entity.User;
import com.hicc.budget.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_missions")
public class UserMission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission; // 미션

    @Column(precision = 10, scale = 2)
    private BigDecimal currentAmount = BigDecimal.ZERO; // 현재 달성 금액

    @Column(nullable = false)
    private boolean isCompleted = false; // 미션 완료 여부

    private LocalDate completedAt; // 미션 완료 일자

    @Column(columnDefinition = "TEXT")
    private String memo; // 사용자 메모

    @Builder
    public UserMission(User user, Mission mission, String memo) {
        this.user = user;
        this.mission = mission;
        this.memo = memo;
    }

    // 현재 달성 금액 업데이트
    public void updateCurrentAmount(BigDecimal amount) {
        if (amount == null) {
            this.currentAmount = BigDecimal.ZERO;
        } else {
            this.currentAmount = amount.compareTo(BigDecimal.ZERO) >= 0 ? amount : BigDecimal.ZERO;
        }
        checkCompletion();
    }

    // 달성 금액 추가
    public void addAmount(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.currentAmount = this.currentAmount.add(amount);
            checkCompletion();
        }
    }

    // 미션 완료 상태 확인 및 업데이트
    private void checkCompletion() {
        if (mission.getTargetAmount() != null && !isCompleted) {
            if (currentAmount.compareTo(mission.getTargetAmount()) >= 0) {
                completeMission();
            }
        }
    }

    // 미션 완료 처리
    public void completeMission() {
        if (!isCompleted) {
            this.isCompleted = true;
            this.completedAt = LocalDate.now();
        }
    }

    // 미션 재시작
    public void restartMission() {
        this.isCompleted = false;
        this.completedAt = null;
        this.currentAmount = BigDecimal.ZERO;
    }

    // 메모 업데이트
    public void updateMemo(String memo) {
        this.memo = memo;
    }

    // 달성률 계산 (0~100)
    public int getAchievementRate() {
        if (mission.getTargetAmount() == null || mission.getTargetAmount().compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        
        double rate = currentAmount.doubleValue() / mission.getTargetAmount().doubleValue() * 100;
        return (int) Math.min(100, Math.max(0, Math.round(rate)));
    }
}
