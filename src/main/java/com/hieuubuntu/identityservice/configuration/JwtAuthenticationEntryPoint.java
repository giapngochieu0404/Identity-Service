package com.hieuubuntu.identityservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hieuubuntu.identityservice.dto.response.DefaultResponse;
import com.hieuubuntu.identityservice.exception.error_code.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Catch 401:
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setCode(errorCode.getCode());
        defaultResponse.setMessage(errorCode.getMessage());
        defaultResponse.setSuccess(false);

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(defaultResponse));

        response.flushBuffer();
    }
}
