package com.hicc.budget.dto.saving;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingSummaryResponse {
    
    private String month; // YYYY-MM
    private BigDecimal totalIncome; // 총 수입
    private BigDecimal totalExpense; // 총 지출
    private BigDecimal totalSaving; // 총 절약액 (수입 - 지출)
    private BigDecimal budget; // 예산 (이번 달 목표 지출액, 설정된 경우)
    private BigDecimal remainingBudget; // 남은 예산 (예산 - 총 지출)
    private int achievementRate; // 예산 대비 달성률 (%)
    
    // 카테고리별 지출 내역 (선택사항, 필요한 경우 추가)
    // private List<CategoryExpense> categoryExpenses;
    
    // 예시 정적 팩토리 메소드
    public static SavingSummaryResponse create(String month, BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal budget) {
        BigDecimal totalSaving = totalIncome.subtract(totalExpense);
        BigDecimal remainingBudget = budget != null ? budget.subtract(totalExpense) : null;
        
        int achievementRate = 0;
        if (budget != null && budget.compareTo(BigDecimal.ZERO) > 0) {
            double rate = totalExpense.doubleValue() / budget.doubleValue() * 100;
            achievementRate = (int) Math.min(100, Math.max(0, Math.round(rate)));
        }
        
        return SavingSummaryResponse.builder()
                .month(month)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .totalSaving(totalSaving)
                .budget(budget)
                .remainingBudget(remainingBudget)
                .achievementRate(achievementRate)
                .build();
    }
}
