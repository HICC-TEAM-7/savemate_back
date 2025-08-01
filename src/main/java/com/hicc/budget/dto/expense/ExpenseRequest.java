package com.hicc.budget.dto.expense;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @Size(max = 500, message = "Memo must be less than 500 characters")
    private String memo;
    
    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    // 영수증 이미지 URL (선택사항)
    private String receiptImageUrl;
}
