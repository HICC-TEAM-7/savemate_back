package com.hicc.budget.service.auth;

import com.hicc.budget.domain.user.entity.User;
import com.hicc.budget.domain.user.entity.UserRole;
import com.hicc.budget.domain.user.entity.SocialType;
import com.hicc.budget.domain.user.repository.UserRepository;
import com.hicc.budget.dto.auth.AuthResponse;
import com.hicc.budget.dto.auth.LoginRequest;
import com.hicc.budget.dto.auth.SignUpRequest;
import com.hicc.budget.dto.auth.SignUpResponse;
import com.hicc.budget.global.exception.BusinessException;
import com.hicc.budget.global.exception.ErrorCode;
import com.hicc.budget.global.exception.custom.UserNotFoundException;
import com.hicc.budget.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 닉네임 중복 확인
        if (userRepository.existsByNickname(signUpRequest.getNickname())) {
            throw new BusinessException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        // 사용자 생성
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(encodedPassword)
                .nickname(signUpRequest.getNickname())
                .role(UserRole.USER)
                .socialType(SocialType.EMAIL)
                .build();
                
        // 알림 설정 (기본값 true로 설정)
        user.setNotificationEnabled(true);

        // 사용자 저장
        User savedUser = userRepository.save(user);

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(savedUser.getId(), savedUser.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(savedUser.getId());

        // 리프레시 토큰 저장
        savedUser.updateRefreshToken(refreshToken);
        userRepository.save(savedUser);

        return SignUpResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 사용자 정보 조회
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        // 리프레시 토큰 저장
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isNewUser(false) // 기존 사용자
                .user(AuthResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .profileImageUrl(user.getProfileImageUrl())
                        .build())
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        // 리프레시 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UserNotFoundException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 사용자 조회
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        // 저장된 리프레시 토큰과 일치하는지 확인
        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new UserNotFoundException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 새로운 액세스 토큰 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        // 리프레시 토큰 갱신
        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .isNewUser(false)
                .user(AuthResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .profileImageUrl(user.getProfileImageUrl())
                        .build())
                .build();
    }
}
