package hicc.budget.demo.MissionMatch.dto;

import hicc.budget.demo.MissionMatch.domain.MissionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class MissionMatchResponse {

    Long id;
    Long missionId;
    Long user1Id;
    Long user2Id;
    LocalDate startedAt;
    MissionStatus status;
}
