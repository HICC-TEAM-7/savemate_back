package hicc.budget.demo.UserMission.repository;

import hicc.budget.demo.UserMission.domain.UserMission;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface UserMissionRepository extends JpaRepository<UserMission,Long> {
    Page<UserMission> findByUser_IdAndStatus(Long userId, UserMission.Status status, Pageable pageable);
}
