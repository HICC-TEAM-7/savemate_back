package com.hicc.budget.repository;

import com.hicc.budget.domain.category.entity.Category;
import com.hicc.budget.domain.category.entity.CategoryType;
import com.hicc.budget.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // 사용자 ID와 카테고리 타입으로 카테고리 목록 조회
    List<Category> findByUserIdAndType(Long userId, CategoryType type);
    
    // 기본 제공 카테고리 목록 조회
    List<Category> findByIsDefaultTrue();
    
    // 사용자 정의 카테고리 목록 조회
    List<Category> findByUser(User user);
    
    // 사용자 ID와 카테고리 ID로 카테고리 조회
    Optional<Category> findByIdAndUserId(Long id, Long userId);
}
