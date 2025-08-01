package com.hicc.budget.repository;

import com.hicc.budget.domain.friend.entity.Friend;
import com.hicc.budget.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    
    // 사용자의 친구 목록 조회 (차단 제외)
    List<Friend> findByFollowerAndIsBlockedFalse(User follower);
    
    // 사용자를 팔로우하는 팔로워 목록 조회 (차단 제외)
    List<Friend> findByFollowingAndIsBlockedFalse(User following);
    
    // 두 사용자 간의 친구 관계 조회
    Optional<Friend> findByFollowerAndFollowing(User follower, User following);
    
    // 사용자의 친구 수 조회
    long countByFollowerAndIsBlockedFalse(User follower);
    
    // 사용자를 팔로우하는 팔로워 수 조회
    long countByFollowingAndIsBlockedFalse(User following);
    
    // 사용자의 차단된 친구 목록 조회
    List<Friend> findByFollowerAndIsBlockedTrue(User follower);
    
    // 사용자 간의 친구 관계 존재 여부 확인
    boolean existsByFollowerAndFollowing(User follower, User following);
    
    // 사용자 ID로 친구 목록 조회 (JPQL로 성능 최적화)
    @Query("SELECT f FROM Friend f " +
           "JOIN FETCH f.following u " +
           "WHERE f.follower.id = :userId AND f.isBlocked = false")
    List<Friend> findFriendsWithUserDetails(@Param("userId") Long userId);
}
