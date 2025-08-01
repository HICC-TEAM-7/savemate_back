package com.hicc.budget.dto.user;

import com.hicc.budget.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleResponse {
    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;

    public static UserSimpleResponse from(User user) {
        return UserSimpleResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
