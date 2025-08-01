package com.hicc.budget.domain.expense.entity;

import com.hicc.budget.domain.category.entity.Category;
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
public class Expense extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 지출 제목

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount; // 지출 금액

    @Column(columnDefinition = "TEXT")
    private String memo; // 메모

    @Column(nullable = false)
    private LocalDate expenseDate; // 지출 일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // 카테고리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자

    private String receiptImageUrl; // 영수증 이미지 URL

    @Column(nullable = false)
    private boolean isExcluded = false; // 통계 제외 여부

    @Builder
    public Expense(String title, BigDecimal amount, String memo, LocalDate expenseDate, 
                  Category category, User user, String receiptImageUrl) {
        this.title = title;
        this.amount = amount != null ? amount : BigDecimal.ZERO;
        this.memo = memo;
        this.expenseDate = expenseDate != null ? expenseDate : LocalDate.now();
        this.category = category;
        this.user = user;
        this.receiptImageUrl = receiptImageUrl;
    }

    // 지출 정보 업데이트
    public void update(String title, BigDecimal amount, String memo, LocalDate expenseDate, 
                      Category category, String receiptImageUrl) {
        this.title = title != null ? title : this.title;
        this.amount = amount != null ? amount : this.amount;
        this.memo = memo != null ? memo : this.memo;
        this.expenseDate = expenseDate != null ? expenseDate : this.expenseDate;
        this.category = category != null ? category : this.category;
        this.receiptImageUrl = receiptImageUrl != null ? receiptImageUrl : this.receiptImageUrl;
    }

    // 통계 제외 여부 토글
    public void toggleExclude() {
        this.isExcluded = !this.isExcluded;
    }
}
