package hicc.budget.demo.Mission.dto;

import hicc.budget.demo.Mission.domain.Mission;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MissionResponseDto {

    private Long missionId;
    private String title;
    private String description;
    private Long targetAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;

    public static MissionResponseDto from(Mission m) {
        return MissionResponseDto.builder()
                .missionId(m.getId())
                .title(m.getTitle())
                .description(m.getDescription())
                .targetAmount(m.getTargetAmount())
                .startDate(m.getStartDate())
                .endDate(m.getEndDate())
                .isActive(m.isActive())
                .build();
    }
}
