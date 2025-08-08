package hicc.budget.demo.Mission.dto;

import hicc.budget.demo.Mission.domain.Mission;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MissionCreateRequestDto {

    @NotBlank(message = "미션 제목은 필수입니다.")
    private String title;

    // 상세 설명은 선택
    private String description;

    // 금액은 선택. 보낼 경우 0 이상
    @PositiveOrZero(message = "목표 금액은 0 이상이어야 합니다.")
    private Long targetAmount;

    @NotNull(message = "시작일은 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수입니다.")
    private LocalDate endDate;

    // 선택. null이면 true로 처리
    private Boolean isActive;

    public Mission toEntity() {
        return Mission.builder()
                .title(title)
                .description(description)
                .targetAmount(targetAmount)
                .startDate(startDate)
                .endDate(endDate)
                .isActive(isActive == null ? true : isActive)  // 기본 true
                .build();
    }
}
