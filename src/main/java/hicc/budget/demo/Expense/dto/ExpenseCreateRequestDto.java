package hicc.budget.demo.Expense.dto;

import hicc.budget.demo.Expense.domain.Expense;
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
    private String paymentType;// 카드/현금

    @NotNull(message = "카테고리는 필수입니다.")
    private String paymentCategory;//식비, 교통, 기타 .. 등

    @NotNull(message = "금액은 필수입니다.")
    @Positive(message = "금액은 0보다 커야 합니다.")
    private Long amount;  // 소비 금액

    public PaymentType fromTypeString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("paymentType 값이 null 또는 비어있습니다.");
        }

        try {
            return PaymentType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 paymentType 값: " + value, e);
        }
    }

    public static PaymentCategory fromCategoryString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("카테고리 값이 null 또는 비어있습니다.");
        }

        try {
            return PaymentCategory.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 카테고리 값: " + value, e);
        }
    }


    public Expense toEntity() {
        return Expense.builder()
                .date(date)
                .paymentType(fromTypeString(paymentType))
                .paymentCategory(fromCategoryString(paymentCategory))
                .amount(amount)
                .build();
    }






}
