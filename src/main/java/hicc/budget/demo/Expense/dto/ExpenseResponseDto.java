package hicc.budget.demo.Expense.dto;

import hicc.budget.demo.Expense.domain.Expense;
import hicc.budget.demo.Expense.domain.PaymentCategory;
import hicc.budget.demo.Expense.domain.PaymentType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
public class ExpenseResponseDto {

    private Long expenseId;
    private LocalDate date;
    private PaymentType paymentType;
    private PaymentCategory category;
    private Long amount;



    public static ExpenseResponseDto from(Expense expense) {
        return ExpenseResponseDto.builder()
                .expenseId(expense.getId())
                .date(expense.getDate())
                .paymentType(expense.getPaymentType())
                .category(expense.getPaymentCategory())
                .amount(expense.getAmount())
                .build();
    }
}
