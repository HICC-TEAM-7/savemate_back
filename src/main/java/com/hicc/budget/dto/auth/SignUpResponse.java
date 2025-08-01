package com.hicc.budget.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponse {
    private Long id;
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;
}
