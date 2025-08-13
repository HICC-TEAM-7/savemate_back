package hicc.budget.demo.MissionMatch.controller;

import hicc.budget.demo.MissionMatch.dto.MissionMatchRequest;
import hicc.budget.demo.MissionMatch.dto.MissionMatchResponse;
import hicc.budget.demo.MissionMatch.service.MissionMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mission-matches")
public class MissionMatchController {

    private final MissionMatchService missionMatchService;

    //미션 매치 생성
    @PostMapping
    public ResponseEntity<MissionMatchResponse> createMissionMatch(
            @Validated @RequestBody MissionMatchRequest request
    ) {
        MissionMatchResponse response = missionMatchService.create(request);
        return ResponseEntity.ok(response);
    }

    //내가 참여한 매칭 목록 조회
    @GetMapping
    public ResponseEntity<Page<MissionMatchResponse>> getMyMatches(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<MissionMatchResponse> matches = missionMatchService.getMyMatches(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(matches);
    }
}
