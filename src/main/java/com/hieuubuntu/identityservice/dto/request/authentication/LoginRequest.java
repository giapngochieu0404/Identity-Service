package com.hieuubuntu.identityservice.dto.request.authentication;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotNull(message = "Thiếu tên đăng nhập")
    private String username;

    @NotNull(message = "Thiếu mật khẩu")
    private String password;
}
