package com.hieuubuntu.identityservice.dto.request.authentication;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
    @NotNull(message = "Thiếu token")
    private String token;
}
