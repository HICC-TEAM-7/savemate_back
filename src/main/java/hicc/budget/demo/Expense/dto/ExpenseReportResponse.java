package hicc.budget.demo.Expense.dto;

import hicc.budget.demo.Expense.domain.PaymentCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ExpenseReportResponse {

    // 리포트 기간 정보
    @Getter
    @Builder
    public static class Period {
        private LocalDate startDate; // 시작 날짜
        private LocalDate endDate;   // 종료 날짜
    }

    // 카테고리별 합계/퍼센트 정보
    @Getter
    @Builder
    public static class CategoryItem {
        private PaymentCategory category; // 카테고리
        private Long amount;              // 총 금액
        private int percentage;           // 비율 (0~100)
    }

    private Period period;                     // 리포트 기간
    private Long totalAmount;                   // 전체 합계 금액
    private List<CategoryItem> categoryReport;  // 카테고리별 리포트
}
