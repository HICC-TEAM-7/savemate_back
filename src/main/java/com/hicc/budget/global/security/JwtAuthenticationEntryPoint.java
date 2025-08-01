package com.hicc.budget.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hicc.budget.global.exception.ErrorCode;
import com.hicc.budget.global.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
        


        response.setContentType("application/json;charset=UTF-8");ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.UNAUTHORIZED,
                "인증에 실패했습니다. 유효한 인증 정보가 필요합니다.",
                request.getRequestURI()
        );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
