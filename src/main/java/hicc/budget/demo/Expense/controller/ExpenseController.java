package hicc.budget.demo.Expense.controller;

import hicc.budget.demo.Expense.domain.PaymentCategory;
import hicc.budget.demo.Expense.domain.PaymentType;
import hicc.budget.demo.Expense.dto.ExpenseCreateRequestDto;
import hicc.budget.demo.Expense.dto.ExpenseReportResponse;
import hicc.budget.demo.Expense.dto.ExpenseResponseDto;
import hicc.budget.demo.Expense.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    /** 지출 등록 */
    @PostMapping
    public ResponseEntity<ExpenseResponseDto> create(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody ExpenseCreateRequestDto req) {
        return ResponseEntity.ok(expenseService.create(userId, req));
    }

    /** 단건 조회 */
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponseDto> getOne(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long expenseId) {
        return ResponseEntity.ok(expenseService.getOne(userId, expenseId));
    }

    /** 월별 목록 조회 */
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> listByMonth(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) PaymentCategory category,
            @RequestParam(required = false) PaymentType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return ResponseEntity.ok(
                expenseService.listByMonth(userId, year, month, category, type, page, size)
        );
    }

    /** 주별 리포트 */
    @GetMapping("/report/weekly")
    public ResponseEntity<ExpenseReportResponse> weekly(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(expenseService.weekly(userId, date));
    }

    /** 월별 리포트 */
    @GetMapping("/report/monthly")
    public ResponseEntity<ExpenseReportResponse> monthly(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(expenseService.monthly(userId, year, month));
    }
}
