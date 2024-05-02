package com.hieuubuntu.identityservice.dto.request.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    @NotNull(message = "Thiếu username")
    private String username;

    @Size(min = 8, message = "Mật khẩu phải tối thiểu 8 kí tự")
    private String password;

    @NotNull(message = "Thiếu full name")
    private String fullname;
}
