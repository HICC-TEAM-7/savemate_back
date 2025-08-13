package hicc.budget.demo.Mission.service;

import hicc.budget.demo.Mission.domain.Mission;
import hicc.budget.demo.Mission.domain.UserMission;
import hicc.budget.demo.Mission.dto.MissionCreateRequestDto;
import hicc.budget.demo.Mission.dto.MissionResponseDto;
import hicc.budget.demo.Mission.dto.MyAppliedMissionDto;
import hicc.budget.demo.Mission.repository.MissionRepository;
import hicc.budget.demo.Mission.repository.UserMissionRepository;
import hicc.budget.demo.User.domain.User;
import hicc.budget.demo.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final UserRepository userRepository;

    /** 미션 생성 */
    public MissionResponseDto createMission(MissionCreateRequestDto req) {
        if (req.getStartDate().isAfter(req.getEndDate())) {
            throw new IllegalArgumentException("시작일이 종료일보다 늦을 수 없습니다.");
        }
        Mission saved = missionRepository.save(req.toEntity());
        return MissionResponseDto.from(saved);
    }

    //미션 단일
    @Transactional(readOnly = true)
    public MissionResponseDto getMission(Long missionId) {
        return missionRepository.findById(missionId)
                .map(MissionResponseDto::from)
                .orElseThrow(() -> new IllegalArgumentException("해당 미션이 없습니다. id=" + missionId));
    }

    /** 전체 미션 목록 (활성만) */
    @Transactional(readOnly = true)
    public List<MissionResponseDto> getAllMissions() {
        return missionRepository.findByIsActiveTrue()
                .stream()
                .map(MissionResponseDto::from)
                .collect(toList());
    }

    /** 일일 추천 미션 목록 */
    @Transactional(readOnly = true)
    public List<MissionResponseDto> getDailyRecommendedMissions(Long userId, int size) {
        LocalDate today = LocalDate.now();

        List<Mission> candidates = missionRepository
                .findByIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        today, today, PageRequest.of(0, size * 2)
                );

        List<Long> alreadyAppliedIds = userMissionRepository.findByUser_Id(userId)
                .stream().map(um -> um.getMission().getId()).collect(toList());

        return candidates.stream()
                .filter(m -> !alreadyAppliedIds.contains(m.getId()))
                .limit(size)
                .map(MissionResponseDto::from)
                .collect(toList());
    }

    /** 내가 신청한 미션 목록 (요약) */
    @Transactional(readOnly = true)
    public List<MyAppliedMissionDto> getMyAppliedMissions(Long userId) {
        return userMissionRepository.findByUser_Id(userId).stream()
                .map(um -> {
                    Mission m = um.getMission();
                    return MyAppliedMissionDto.builder()
                            .missionId(m.getId())
                            .title(m.getTitle())
                            .startDate(m.getStartDate())
                            .endDate(m.getEndDate())
                            .build();
                })
                .collect(toList());
    }

    /** 미션 신청 */
    public void applyMission(Long userId, Long missionId) {
        if (userMissionRepository.existsByUser_IdAndMission_Id(userId, missionId)) {
            throw new IllegalStateException("이미 신청한 미션입니다.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다. id=" + userId));
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("미션을 찾을 수 없습니다. id=" + missionId));

        userMissionRepository.save(
                UserMission.builder()
                        .user(user)
                        .mission(mission)
                        .addedDate(LocalDate.now())
                        .status(UserMission.Status.ACTIVE)
                        .build()
        );
    }
}
