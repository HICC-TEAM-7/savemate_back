package com.hicc.budget.service.auth;

import com.hicc.budget.dto.auth.AuthResponse;
import com.hicc.budget.dto.auth.LoginRequest;
import com.hicc.budget.dto.auth.SignUpRequest;
import com.hicc.budget.dto.auth.SignUpResponse;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(String refreshToken);
    SignUpResponse signUp(SignUpRequest signUpRequest);
}
