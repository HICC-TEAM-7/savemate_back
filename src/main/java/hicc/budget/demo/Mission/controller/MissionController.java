package hicc.budget.demo.Mission.controller;

import hicc.budget.demo.Mission.dto.MissionCreateRequestDto;
import hicc.budget.demo.Mission.dto.MissionResponseDto;
import hicc.budget.demo.Mission.dto.MyAppliedMissionDto;
import hicc.budget.demo.Mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    /** 미션 생성 */
    @PostMapping
    public ResponseEntity<MissionResponseDto> createMission(@RequestBody MissionCreateRequestDto req) {
        return ResponseEntity.ok(missionService.createMission(req));
    }

    /** 전체 미션 목록 (활성) */
    @GetMapping
    public ResponseEntity<List<MissionResponseDto>> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    /** 일일 추천 미션 목록 */
    @GetMapping("/daily")
    public ResponseEntity<List<MissionResponseDto>> getDailyRecommendedMissions(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(missionService.getDailyRecommendedMissions(userId, size));
    }

    /** 내가 신청한 미션 목록 */
    @GetMapping("/my-missions")
    public ResponseEntity<List<MyAppliedMissionDto>> getMyAppliedMissions(@RequestParam Long userId) {
        return ResponseEntity.ok(missionService.getMyAppliedMissions(userId));
    }

    /** 미션 신청 */
    @PostMapping("/{missionId}/apply")
    public ResponseEntity<Void> applyMission(
            @RequestParam Long userId,
            @PathVariable Long missionId) {
        missionService.applyMission(userId, missionId);
        return ResponseEntity.ok().build();
    }
}
