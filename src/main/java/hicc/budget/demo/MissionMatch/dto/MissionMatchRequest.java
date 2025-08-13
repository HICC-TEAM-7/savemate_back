package hicc.budget.demo.MissionMatch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissionMatchRequest {
    @NotNull private Long MissionId;
    @NotNull private Long user1Id;
    @NotNull private  Long user2Id;
    //null이면 서비스에서 today로 처리..
    private LocalDate startedAt;
}
