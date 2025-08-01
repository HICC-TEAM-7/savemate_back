package com.hicc.budget.repository;

import com.hicc.budget.domain.mission.entity.Mission;
import com.hicc.budget.domain.mission.entity.MissionType;
import com.hicc.budget.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    
    // 활성화된 공개 미션 목록 조회 (시스템 제공 + 다른 사용자 공개 미션)
    @Query("SELECT m FROM Mission m WHERE (m.creator IS NULL OR m.creator = :user) " +
           "AND m.isActive = true AND (m.isPublic = true OR m.creator = :user) " +
           "AND (m.startDate <= CURRENT_DATE AND m.endDate >= CURRENT_DATE)")
    Page<Mission> findActiveMissions(@Param("user") User user, Pageable pageable);
    
    // 사용자가 생성한 미션 목록 조회
    List<Mission> findByCreator(User creator);
    
    // 특정 유형의 미션 목록 조회 (SYSTEM or USER)
    Page<Mission> findByType(MissionType type, Pageable pageable);
    
    // 기간 내에 종료되는 미션 목록 조회 (알림용)
    List<Mission> findByEndDateBetween(LocalDate startDate, LocalDate endDate);
    
    // 사용자가 진행 중인 미션 목록 조회 (UserMission과 조인)
    @Query("SELECT m FROM Mission m JOIN UserMission um ON m.id = um.mission.id " +
           "WHERE um.user = :user AND um.isCompleted = false " +
           "AND m.endDate >= CURRENT_DATE")
    List<Mission> findInProgressMissionsByUser(@Param("user") User user);
}
