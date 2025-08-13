package hicc.budget.demo.MissionMatch.domain;

import hicc.budget.demo.Mission.domain.Mission;
import hicc.budget.demo.User.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="mission_match", uniqueConstraints = {
        // 같은 미션에서 같은 두 유저 페어를 중복 매칭하지 않도록(순서 고정이라면 충분)
        @UniqueConstraint(name = "uk_mission_user1_user2", columnNames = {"mission_id", "user1_id", "user2_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 매칭 시작일
    @Column(name = "started_at", nullable = false)
    private LocalDate startedAt;

    // 매칭 진행 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MissionStatus status;

    //미션 매치는 항상 하나의 mission에 속함
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    //매칭 참가자 1
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    // 매칭 참가자 2 (N:1)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    //편의 메서드 ( 상태 변경 )
    public void progress(){this.status = MissionStatus.IN_PROGRESS;}
    public void complete(){this.status = MissionStatus.COMPLETE;}

}
