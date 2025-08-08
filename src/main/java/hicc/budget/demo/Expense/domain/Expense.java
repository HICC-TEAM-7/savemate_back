package hicc.budget.demo.Expense.domain;

import hicc.budget.demo.User.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "expense")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 소비 ID

    private LocalDate date; // 소비 날짜

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;//소비 방식

    @Enumerated(EnumType.STRING)
    private PaymentCategory paymentCategory; //소비카테고리

    private Long amount; // 소비 금액

    // 외래키로 User 연결 (user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
