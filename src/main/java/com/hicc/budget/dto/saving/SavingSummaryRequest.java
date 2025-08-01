package com.hicc.budget.dto.saving;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavingSummaryRequest {
    
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "Month must be in YYYY-MM format")
    private String month; // YYYY-MM 형식
    
    // 기본값으로 현재 월 설정
    public SavingSummaryRequest() {
        this.month = java.time.YearMonth.now().toString();
    }
}
