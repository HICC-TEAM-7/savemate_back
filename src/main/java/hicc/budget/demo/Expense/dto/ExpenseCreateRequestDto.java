package hicc.budget.demo.Expense.dto;

import hicc.budget.demo.Expense.domain.PaymentType;
import hicc.budget.demo.Expense.domain.PaymentCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor

//지출 등록
public class ExpenseCreateRequestDto {

    @NotNull(message = "날짜는 필수입니다.")
    private LocalDate date; // 소비 날짜

    @NotNull(message="결제 수단은 필수입니다.")
    private PaymentType paymentType;// 카드/현금

    @NotNull(message = "카테고리는 필수입니다.")
    private PaymentCategory paymentCategory;//식비, 교통, 기타 .. 등

    @NotNull(message = "금액은 필수입니다.")
    @Positive(message = "금액은 0보다 커야 합니다.")
    private Long amount;  // 소비 금액
}
