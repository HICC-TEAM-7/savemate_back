package hicc.budget.demo.Mission.dto;


import hicc.budget.demo.Mission.domain.UserMission;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
//내가 신청한 목록 조회
public class MyAppliedMissionDto {

    private Long missionId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

}
