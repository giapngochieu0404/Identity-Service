package com.hieuubuntu.identityservice.controller;

import java.text.ParseException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.hieuubuntu.identityservice.dto.request.authentication.IntrospectRequest;
import com.hieuubuntu.identityservice.dto.request.authentication.LoginRequest;
import com.hieuubuntu.identityservice.dto.request.authentication.LogoutRequest;
import com.hieuubuntu.identityservice.dto.request.authentication.RefreshTokenRequest;
import com.hieuubuntu.identityservice.dto.response.DefaultResponse;
import com.hieuubuntu.identityservice.dto.response.authentication.AuthenticationResponse;
import com.hieuubuntu.identityservice.dto.response.authentication.IntrospectResponse;
import com.hieuubuntu.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    DefaultResponse<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) throws JOSEException {
        DefaultResponse<AuthenticationResponse> response = new DefaultResponse<>();
        response.setData(authService.login(request));
        return response;
    }

    @PostMapping("/introspect")
    DefaultResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        DefaultResponse<IntrospectResponse> response = new DefaultResponse<>();
        response.setData(authService.introspect(request));
        return response;
    }

    @PostMapping("/logout")
    DefaultResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        DefaultResponse<Void> response = new DefaultResponse<>();
        authService.logout(request);
        return response;
    }

    @PostMapping("/refresh-token")
    DefaultResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        DefaultResponse<AuthenticationResponse> response = new DefaultResponse<>();
        response.setData(authService.refreshToken(request));
        return response;
    }
}
