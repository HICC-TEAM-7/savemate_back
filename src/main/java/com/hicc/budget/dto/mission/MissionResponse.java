package com.hicc.budget.dto.mission;

import com.hicc.budget.domain.mission.entity.Mission;
import com.hicc.budget.domain.mission.entity.MissionType;
import com.hicc.budget.dto.user.UserSimpleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MissionResponse {
    
    private Long id;
    private String title;
    private String description;
    private BigDecimal targetAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isPublic;
    private boolean isActive;
    private MissionType type;
    private UserSimpleResponse creator; // 미션 생성자 정보
    private Integer participantCount; // 참여자 수
    
    public static MissionResponse from(Mission mission) {
        return MissionResponse.builder()
                .id(mission.getId())
                .title(mission.getTitle())
                .description(mission.getDescription())
                .targetAmount(mission.getTargetAmount())
                .startDate(mission.getStartDate())
                .endDate(mission.getEndDate())
                .isPublic(mission.isPublic())
                .isActive(mission.isActive())
                .type(mission.getType())
                .creator(mission.getCreator() != null ? 
                        UserSimpleResponse.from(mission.getCreator()) : null)
                .build();
    }
}
