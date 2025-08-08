package hicc.budget.demo.Mission.repository;



import hicc.budget.demo.Mission.domain.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {

    /** 내가 신청한 미션들 */
    List<UserMission> findByUser_Id(Long userId);

    /** 중복 신청 방지 등 체크 */
    boolean existsByUser_IdAndMission_Id(Long userId, Long missionId);
}
