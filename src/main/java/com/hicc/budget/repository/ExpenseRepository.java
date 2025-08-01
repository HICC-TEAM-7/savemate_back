package com.hicc.budget.repository;

import com.hicc.budget.domain.expense.entity.Expense;
import com.hicc.budget.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    // 사용자 ID와 기간으로 지출 목록 조회 (페이징 처리)
    Page<Expense> findByUserIdAndExpenseDateBetween(
            Long userId, 
            LocalDate startDate, 
            LocalDate endDate, 
            Pageable pageable
    );
    
    // 카테고리별 지출 합계 조회
    @Query("SELECT e.category.id, SUM(e.amount) " +
           "FROM Expense e " +
           "WHERE e.user.id = :userId " +
           "AND e.expenseDate BETWEEN :startDate AND :endDate " +
           "AND e.isExcluded = false " +
           "GROUP BY e.category.id")
    List<Object[]> sumAmountsByCategoryAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    
    // 월별 지출 합계 조회
    @Query("SELECT FUNCTION('DATE_FORMAT', e.expenseDate, '%Y-%m'), SUM(e.amount) " +
           "FROM Expense e " +
           "WHERE e.user.id = :userId " +
           "AND e.expenseDate BETWEEN :startDate AND :endDate " +
           "AND e.isExcluded = false " +
           "GROUP BY FUNCTION('DATE_FORMAT', e.expenseDate, '%Y-%m')")
    List<Object[]> sumAmountsByMonth(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    
    // 사용자 ID와 카테고리 ID로 지출 목록 조회
    List<Expense> findByUserIdAndCategoryId(Long userId, Long categoryId);
    
    // 사용자 ID와 지출 ID로 지출 조회
    Optional<Expense> findByIdAndUserId(Long id, Long userId);
}
