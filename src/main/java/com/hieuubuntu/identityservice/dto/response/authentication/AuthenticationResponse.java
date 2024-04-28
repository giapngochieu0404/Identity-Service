package com.hieuubuntu.identityservice.dto.response.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private boolean authenticated;
    private String token;
}
