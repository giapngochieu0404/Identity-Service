package com.hieuubuntu.IdentityService.exception.type;

import com.hieuubuntu.IdentityService.exception.error_code.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    public AppException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;
}
