package hicc.budget.demo.Expense.repository;

import hicc.budget.demo.Expense.domain.Expense;
import hicc.budget.demo.Expense.domain.PaymentCategory;
import hicc.budget.demo.Expense.domain.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
/*
    @Query("""
      SELECT e FROM Expense e
      WHERE e.user.id = :userId
        AND (:from IS NULL OR e.date >= :from)
        AND (:to   IS NULL OR e.date <= :to)
        AND (:category IS NULL OR e.paymentCategory = :category)
        AND (:type     IS NULL OR e.paymentType = :type)
      ORDER BY e.date DESC, e.id DESC
    """)
    List<Expense> search(
            @Param("userId") Long userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("category") PaymentCategory category,
            @Param("type") PaymentType type,
            Pageable pageable
    );

    @Query("""
      SELECT e FROM Expense e
      WHERE e.user.id = :userId
        AND e.id = :expenseId
    """)
    Expense findOneByUser(@Param("userId") Long userId, @Param("expenseId") Long expenseId);

    // 월별 카테고리 합계 (JPQL)
    @Query("""
      SELECT e.paymentCategory AS category, SUM(e.amount) AS total
      FROM Expense e
      WHERE e.user.id = :userId AND e.date BETWEEN :from AND :to
      GROUP BY e.paymentCategory
      ORDER BY SUM(e.amount) DESC
    """)
    List<Object[]> sumByCategoryMonthly(@Param("userId") Long userId,
                                        @Param("from") LocalDate from,
                                        @Param("to") LocalDate to);

    // 주차별 합계 (MySQL 기준: YEARWEEK(… ,1)) — 네이티브
    @Query(value = """
      SELECT YEARWEEK(date, 1) AS yearWeek, SUM(amount) AS total
      FROM expense
      WHERE user_id = :userId AND date BETWEEN :from AND :to
      GROUP BY YEARWEEK(date, 1)
      ORDER BY yearWeek
    """, nativeQuery = true)
    List<Object[]> sumByYearWeek(@Param("userId") Long userId,
                                 @Param("from") LocalDate from,
                                 @Param("to") LocalDate to);
    */
    Optional<Expense> findByIdAndUser_Id(Long id, Long userId);

    Page<Expense> findByUser_IdAndDateBetweenOrderByDateDescIdDesc(
            Long userId, LocalDate from, LocalDate to, Pageable pageable);

    Page<Expense> findByUser_IdAndDateBetweenAndPaymentCategoryOrderByDateDescIdDesc(
            Long userId, LocalDate from, LocalDate to, PaymentCategory category, Pageable pageable);

    Page<Expense> findByUser_IdAndDateBetweenAndPaymentTypeOrderByDateDescIdDesc(
            Long userId, LocalDate from, LocalDate to, PaymentType type, Pageable pageable);

    Page<Expense> findByUser_IdAndDateBetweenAndPaymentCategoryAndPaymentTypeOrderByDateDescIdDesc(
            Long userId, LocalDate from, LocalDate to, PaymentCategory category, PaymentType type, Pageable pageable);
}
