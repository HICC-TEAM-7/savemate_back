package com.hicc.budget.repository;

import com.hicc.budget.domain.user.entity.User;
import com.hicc.budget.domain.user.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);
    
    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);
    
    // 소셜 타입과 소셜 아이디로 사용자 조회
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
    
    // 닉네임 존재 여부 확인
    boolean existsByNickname(String nickname);
}
