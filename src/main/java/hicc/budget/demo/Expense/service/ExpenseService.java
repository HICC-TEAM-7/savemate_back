package hicc.budget.demo.Expense.service;

import hicc.budget.demo.Expense.domain.Expense;
import hicc.budget.demo.Expense.domain.PaymentCategory;
import hicc.budget.demo.Expense.domain.PaymentType;
import hicc.budget.demo.Expense.dto.ExpenseCreateRequestDto;
import hicc.budget.demo.Expense.dto.ExpenseResponseDto;
import hicc.budget.demo.Expense.dto.ExpenseReportResponse;
import hicc.budget.demo.Expense.repository.ExpenseRepository;
import hicc.budget.demo.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    /** 지출 등록 */
    public ExpenseResponseDto create(Long userId, ExpenseCreateRequestDto req) {
        var user = userRepository.getReferenceById(userId);
        Expense expense = Expense.builder()
                .date(req.getDate())
                .paymentType(req.getPaymentType())
                .paymentCategory(req.getPaymentCategory())
                .amount(req.getAmount())
                .user(user)
                .build();
        return ExpenseResponseDto.from(expenseRepository.save(expense));
    }

    /** 단건 조회 */
    @Transactional(readOnly = true)
    public ExpenseResponseDto getOne(Long userId, Long expenseId) {
        Expense expense = expenseRepository.findByIdAndUser_Id(expenseId, userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않거나 권한 없음"));
        return ExpenseResponseDto.from(expense);
    }

    /** 월별 목록 조회 */
    @Transactional(readOnly = true)
    public List<ExpenseResponseDto> listByMonth(Long userId, int year, int month,
                                                PaymentCategory category, PaymentType type,
                                                int page, int size) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        var pageable = PageRequest.of(page, size);

        if (category != null && type != null) {
            return expenseRepository
                    .findByUser_IdAndDateBetweenAndPaymentCategoryAndPaymentTypeOrderByDateDescIdDesc(
                            userId, start, end, category, type, pageable)
                    .map(ExpenseResponseDto::from)
                    .toList();
        } else if (category != null) {
            return expenseRepository
                    .findByUser_IdAndDateBetweenAndPaymentCategoryOrderByDateDescIdDesc(
                            userId, start, end, category, pageable)
                    .map(ExpenseResponseDto::from)
                    .toList();
        } else if (type != null) {
            return expenseRepository
                    .findByUser_IdAndDateBetweenAndPaymentTypeOrderByDateDescIdDesc(
                            userId, start, end, type, pageable)
                    .map(ExpenseResponseDto::from)
                    .toList();
        } else {
            return expenseRepository
                    .findByUser_IdAndDateBetweenOrderByDateDescIdDesc(
                            userId, start, end, pageable)
                    .map(ExpenseResponseDto::from)
                    .toList();
        }
    }

    /** 주별 리포트 */
    @Transactional(readOnly = true)
    public ExpenseReportResponse weekly(Long userId, LocalDate dateInWeek) {
        LocalDate start = dateInWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate end = dateInWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return buildReport(userId, start, end);
    }

    /** 월별 리포트 */
    @Transactional(readOnly = true)
    public ExpenseReportResponse monthly(Long userId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return buildReport(userId, start, end);
    }

    /** 공용 리포트 생성 메서드 */
    private ExpenseReportResponse buildReport(Long userId, LocalDate start, LocalDate end) {
        var list = expenseRepository.findByUser_IdAndDateBetweenOrderByDateDescIdDesc(
                userId, start, end, PageRequest.of(0, 10_000)).getContent();

        long total = list.stream().mapToLong(Expense::getAmount).sum();
        var byCat = list.stream().collect(Collectors.groupingBy(
                Expense::getPaymentCategory,
                Collectors.summingLong(Expense::getAmount)
        ));

        var categoryReport = byCat.entrySet().stream()
                .map(e -> ExpenseReportResponse.CategoryItem.builder()
                        .category(e.getKey())
                        .amount(e.getValue())
                        .percentage(total == 0 ? 0 :
                                (int) Math.round(e.getValue() * 100.0 / total))
                        .build())
                .sorted(Comparator.comparingLong(ExpenseReportResponse.CategoryItem::getAmount).reversed())
                .toList();

        return ExpenseReportResponse.builder()
                .period(ExpenseReportResponse.Period.builder()
                        .startDate(start).endDate(end).build())
                .totalAmount(total)
                .categoryReport(categoryReport)
                .build();
    }
}
