package com.hicc.budget.dto.auth;

import com.hicc.budget.domain.user.entity.SocialType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Nickname is required")
    @Size(min = 2, max = 20, message = "Nickname must be between 2 and 20 characters")
    private String nickname;
    
    // 소셜 로그인인 경우 비밀번호는 선택사항
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one letter, one number, and one special character")
    private String password;
    
    // 소셜 로그인인 경우
    private SocialType socialType;
    private String socialId;
    private String profileImageUrl;
    
    // FCM 토큰 (선택사항)
    private String fcmToken;
}
