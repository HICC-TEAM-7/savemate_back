package com.hicc.budget.domain.user.repository;

import com.hicc.budget.domain.user.entity.User;
import com.hicc.budget.domain.user.entity.UserRole;
import com.hicc.budget.domain.user.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByRefreshToken(String refreshToken);
}
