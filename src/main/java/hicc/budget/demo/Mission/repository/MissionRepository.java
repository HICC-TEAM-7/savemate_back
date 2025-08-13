package hicc.budget.demo.Mission.repository;


import hicc.budget.demo.Mission.domain.Mission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    /** 활성화된 전체 목록 */
    List<Mission> findByIsActiveTrue();

    //미션 id 로 찾기
    Optional<Mission> findById(Long missionId);

    /** 오늘 진행 중인(기간 안) 미션 (추천용 기본 조건) */
    List<Mission> findByIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDate today1, LocalDate today2, Pageable pageable
    );
}
