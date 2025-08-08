package hicc.budget.demo.Mission.domain;

import hicc.budget.demo.User.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="user_mission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMission {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    //누가 미션 신청했니 ..
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="mission_id")
    private Mission mission;

    private LocalDate addedDate;       // 참여일
    private LocalDate endDate;         // 종료일(기본 mission.endDate)
    @Enumerated(EnumType.STRING)
    private Status status;             // ACTIVE/COMPLETED/CANCELED

    public enum Status { ACTIVE, COMPLETED, CANCELED }
}
