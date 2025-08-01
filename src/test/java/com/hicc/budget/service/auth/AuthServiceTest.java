package com.hicc.budget.service.auth;

import com.hicc.budget.domain.user.entity.SocialType;
import com.hicc.budget.domain.user.entity.User;
import com.hicc.budget.domain.user.entity.UserRole;
import com.hicc.budget.domain.user.repository.UserRepository;
import com.hicc.budget.dto.auth.AuthResponse;
import com.hicc.budget.dto.auth.LoginRequest;
import com.hicc.budget.global.exception.ErrorCode;
import com.hicc.budget.global.exception.custom.UserNotFoundException;
import com.hicc.budget.global.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private final String testEmail = "test@example.com";
    private final String testPassword = "password123";
    private final String encodedPassword = "encodedPassword123";
    private final String accessToken = "testAccessToken";
    private final String refreshToken = "testRefreshToken";

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email(testEmail)
                .password(encodedPassword)
                .nickname("Test User")
                .role(UserRole.USER)
                .socialType(SocialType.EMAIL)
                .notificationEnabled(true)
                .build();
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        LoginRequest loginRequest = new LoginRequest(testEmail, testPassword);
        
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(testPassword, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(any(), any())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(any())).thenReturn(refreshToken);

        // when
        AuthResponse response = authService.login(loginRequest);

        // then
        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertFalse(response.isNewUser());
        assertNotNull(response.getUser());
        assertEquals(testUser.getId(), response.getUser().getId());
        assertEquals(testUser.getEmail(), response.getUser().getEmail());
        assertEquals(testUser.getNickname(), response.getUser().getNickname());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void login_Fail_UserNotFound() {
        // given
        LoginRequest loginRequest = new LoginRequest("nonexistent@example.com", testPassword);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when & then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> authService.login(loginRequest)
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_Fail_InvalidPassword() {
        // given
        LoginRequest loginRequest = new LoginRequest(testEmail, "wrongPassword");
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // when & then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> authService.login(loginRequest)
        );
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }
}
