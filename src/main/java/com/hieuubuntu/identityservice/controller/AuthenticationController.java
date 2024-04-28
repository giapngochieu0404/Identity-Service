package com.hieuubuntu.identityservice.controller;

import com.hieuubuntu.identityservice.dto.request.authentication.IntrospectRequest;
import com.hieuubuntu.identityservice.dto.request.authentication.LoginRequest;
import com.hieuubuntu.identityservice.dto.response.DefaultResponse;
import com.hieuubuntu.identityservice.dto.response.authentication.AuthenticationResponse;
import com.hieuubuntu.identityservice.dto.response.authentication.IntrospectResponse;
import com.hieuubuntu.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authService;

    @PostMapping("/login")
    DefaultResponse<AuthenticationResponse> create(@RequestBody @Valid LoginRequest request) throws JOSEException {
        DefaultResponse<AuthenticationResponse> response = new DefaultResponse<>();
        response.setData(authService.login(request));
        return response;
    }

    @PostMapping("/introspect")
    DefaultResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        DefaultResponse<IntrospectResponse> response = new DefaultResponse<>();
        response.setData(authService.introspect(request));
        return response;
    }
}
