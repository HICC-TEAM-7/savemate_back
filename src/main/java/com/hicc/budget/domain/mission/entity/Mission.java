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
public class Mission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 미션 제목

    @Column(columnDefinition = "TEXT")
    private String description; // 미션 상세 설명

    @Column(precision = 10, scale = 2)
    private BigDecimal targetAmount; // 목표 금액 (null일 수 있음)

    @Column(nullable = false)
    private LocalDate startDate; // 미션 시작일

    @Column(nullable = false)
    private LocalDate endDate; // 미션 종료일

    @Column(nullable = false)
    private boolean isActive = true; // 미션 활성화 여부

    @Column(nullable = false)
    private boolean isPublic = true; // 공개 여부 (다른 사용자에게 표시 여부)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionType type; // 미션 유형 (SYSTEM, USER)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator; // 미션 생성자 (null일 경우 시스템 생성)

    @Builder
    public Mission(String title, String description, BigDecimal targetAmount, 
                  LocalDate startDate, LocalDate endDate, boolean isPublic, 
                  MissionType type, User creator) {
        this.title = title;
        this.description = description;
        this.targetAmount = targetAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPublic = isPublic;
        this.type = type != null ? type : MissionType.USER;
        this.creator = creator;
    }

    // 미션 정보 업데이트
    public void update(String title, String description, BigDecimal targetAmount, 
                      LocalDate startDate, LocalDate endDate, boolean isPublic) {
        this.title = title != null ? title : this.title;
        this.description = description != null ? description : this.description;
        this.targetAmount = targetAmount != null ? targetAmount : this.targetAmount;
        this.startDate = startDate != null ? startDate : this.startDate;
        this.endDate = endDate != null ? endDate : this.endDate;
        this.isPublic = isPublic;
    }

    // 미션 활성화/비활성화
    public void toggleActive() {
        this.isActive = !this.isActive;
    }

    // 미션이 현재 진행 중인지 확인
    public boolean isInProgress() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }
}
