package com.hicc.budget.dto.expense;

import com.hicc.budget.domain.expense.entity.Expense;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    
    private Long id;
    private String title;
    private BigDecimal amount;
    private String memo;
    private LocalDate expenseDate;
    private Long categoryId;
    private String categoryName;
    private String receiptImageUrl;
    private boolean isExcluded;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    
    public static ExpenseResponse from(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .memo(expense.getMemo())
                .expenseDate(expense.getExpenseDate())
                .categoryId(expense.getCategory().getId())
                .categoryName(expense.getCategory().getName())
                .receiptImageUrl(expense.getReceiptImageUrl())
                .isExcluded(expense.isExcluded())
                .createdAt(expense.getCreatedAt().toLocalDate())
                .updatedAt(expense.getUpdatedAt().toLocalDate())
                .build();
    }
}
