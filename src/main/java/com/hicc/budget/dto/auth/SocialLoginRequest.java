package com.hicc.budget.dto.auth;

import com.hicc.budget.domain.user.entity.SocialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginRequest {
    
    @NotBlank(message = "Access token is required")
    private String accessToken;
    
    @NotNull(message = "Social type is required")
    private SocialType socialType;
    
    // FCM 토큰 (선택사항)
    private String fcmToken;
}
