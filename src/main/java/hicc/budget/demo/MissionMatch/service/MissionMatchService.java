package hicc.budget.demo.MissionMatch.service;


import hicc.budget.demo.Mission.domain.Mission;
import hicc.budget.demo.Mission.repository.MissionRepository;
import hicc.budget.demo.MissionMatch.domain.MissionMatch;
import hicc.budget.demo.MissionMatch.domain.MissionStatus;
import hicc.budget.demo.MissionMatch.dto.MissionMatchRequest;
import hicc.budget.demo.MissionMatch.dto.MissionMatchResponse;
import hicc.budget.demo.MissionMatch.repository.MissionMatchRepository;
import hicc.budget.demo.User.domain.User;
import hicc.budget.demo.User.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionMatchService {

    private final MissionMatchRepository missionMatchRepository;
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    //미션 매치 생성 ( 중복 금지 )
    public MissionMatchResponse create(MissionMatchRequest req){
        if(req.getUser1Id().equals(req.getUser2Id())){
            throw new IllegalArgumentException("같은 유저끼리 매칭할 수 없음");
        }

        //정규화 ..
        Long a= Math.min(req.getUser1Id(), req.getUser2Id());
        Long b= Math.max(req.getUser1Id(), req.getUser2Id());

        Mission mission= missionRepository.findById(req.getMissionId()).orElseThrow(()->new IllegalArgumentException("미션을 찾을 수 없음 ."));
        User user1 = userRepository.findById(a)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. id=" + a));
        User user2 = userRepository.findById(b)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. id=" + b));

        boolean exists = missionMatchRepository.existsByMissionAndUser1AndUser2(mission, user1, user2);
        if (exists) {
            throw new IllegalStateException("이미 동일한 매칭이 존재합니다.");
        }
        LocalDate startedAt = (req.getStartedAt() != null) ? req.getStartedAt() : LocalDate.now();

        MissionMatch saved = missionMatchRepository.save(
                MissionMatch.builder()
                        .mission(mission)
                        .user1(user1)
                        .user2(user2)
                        .startedAt(startedAt)
                        .status(MissionStatus.PENDING) // 초기 상태
                        .build()
        );

        return toResponse(saved);
    }

    // 유스케이스 2: 특정 미션의 매칭 목록
    @Transactional(readOnly = true)
    public Page<MissionMatchResponse> getMatchesByMission(Long missionId, Pageable pageable) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션을 찾을 수 없습니다. id=" + missionId));

        return missionMatchRepository.findByMission(mission, pageable)
                .map(this::toResponse);
    }

    // 유스케이스 3: 내가 참여한 매칭 목록
    @Transactional(readOnly = true)
    public Page<MissionMatchResponse> getMyMatches(Long userId, Pageable pageable) {
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. id=" + userId));

        return missionMatchRepository.findByUser1OrUser2(me, me, pageable)
                .map(this::toResponse);
    }

    private MissionMatchResponse toResponse(MissionMatch m) {
        return new MissionMatchResponse(
                m.getId(),
                m.getMission().getId(),
                m.getUser1().getId(),
                m.getUser2().getId(),
                m.getStartedAt(),
                m.getStatus()
        );

    }
}
