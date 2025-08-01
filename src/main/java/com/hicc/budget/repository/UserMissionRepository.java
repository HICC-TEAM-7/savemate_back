package com.hicc.budget.repository;

import com.hicc.budget.domain.mission.entity.Mission;
import com.hicc.budget.domain.mission.entity.UserMission;
import com.hicc.budget.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    
    // 사용자 ID와 미션 ID로 사용자 미션 조회
    Optional<UserMission> findByUserIdAndMissionId(Long userId, Long missionId);
    
    // 사용자의 진행 중인 미션 목록 조회
    List<UserMission> findByUserIdAndIsCompletedFalse(Long userId);
    
    // 사용자의 완료된 미션 목록 조회
    List<UserMission> findByUserIdAndIsCompletedTrue(Long userId);
    
    // 특정 미션을 진행 중인 모든 사용자 미션 조회
    List<UserMission> findByMissionId(Long missionId);
    
    // 사용자의 미션 달성률 조회
    @Query("SELECT AVG(um.achievementRate) FROM UserMission um WHERE um.user.id = :userId")
    Double calculateAverageAchievementRate(@Param("userId") Long userId);
    
    // 기간 내에 완료된 미션 목록 조회 (통계용)
    List<UserMission> findByIsCompletedTrueAndCompletedAtBetween(LocalDate startDate, LocalDate endDate);
}
