package hicc.budget.demo.Mission.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

import lombok.*;
@Entity
@Table(name = "mission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 미션 제목

    @Column(columnDefinition = "TEXT")
    private String description; // 미션 상세 설명

    private Long targetAmount; // 목표 금액 (null일 수 있음)

    @Column(nullable = false)
    private LocalDate startDate; // 미션 시작일

    @Column(nullable = false)
    private LocalDate endDate; // 미션 종료일

    @Column(nullable = false)
    private boolean isActive = true; // 미션 활성화 여부

}
