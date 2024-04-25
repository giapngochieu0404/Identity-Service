package com.hieuubuntu.IdentityService.dto.request.authentication;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntrospectRequest {
    @NotNull(message = "Thiếu tên đăng nhập")
    private String token;
}
