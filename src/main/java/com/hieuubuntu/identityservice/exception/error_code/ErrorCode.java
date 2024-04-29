package com.hieuubuntu.identityservice.exception.error_code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DEFAULT_ERROR(1000, "Có lỗi xảy ra. Vui lòng liên hệ Admin", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PARAMS_REQUEST(1001, "Tham số không hợp lệ", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1002, "unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1003, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),

    // 2000:
    USERNAME_EXISTS(2000, "Username đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(2001, "User không tồn tại", HttpStatus.NOT_FOUND),
    USER_PASSWORD_INCORRECT(2002, "Thông tin tài khoản hoặc mật khẩu không chính xác", HttpStatus.BAD_REQUEST)

    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
