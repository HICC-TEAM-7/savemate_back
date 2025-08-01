package com.hicc.budget.domain.category.entity;

import com.hicc.budget.domain.user.entity.User;
import com.hicc.budget.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String icon; // 아이콘 URL 또는 아이콘 클래스명

    @Column(nullable = false)
    private boolean isDefault; // 기본 제공 카테고리 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 사용자 정의 카테고리인 경우에만 값이 있음

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType type; // INCOME(수입), EXPENSE(지출)

    @Builder
    public Category(String name, String icon, boolean isDefault, User user, CategoryType type) {
        this.name = name;
        this.icon = icon;
        this.isDefault = isDefault;
        this.user = user;
        this.type = type != null ? type : CategoryType.EXPENSE; // 기본값은 지출
    }

    // 카테고리명 업데이트
    public void updateName(String name) {
        this.name = name;
    }

    // 아이콘 업데이트
    public void updateIcon(String icon) {
        this.icon = icon;
    }
}
