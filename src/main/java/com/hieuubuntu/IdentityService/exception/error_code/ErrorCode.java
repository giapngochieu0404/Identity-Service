package com.hieuubuntu.IdentityService.exception.error_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DEFAULT_ERROR(1000, "Có lỗi xảy ra. Vui lòng liên hệ Admin"),
    INVALID_PARAMS_REQUEST(1001, "Tham số không hợp lệ"),

    // 2000:
    USERNAME_EXISTS(2000, "Username đã tồn tại"),
    USER_NOT_EXISTS(2001, "User không tồn tại")


    ;

    private int code;
    private String message;
}
