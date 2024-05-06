package com.hieuubuntu.identityservice.exception.type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CanPermissionException extends RuntimeException{
    public CanPermissionException(String message) {
        super();
        this.message = message;
    }

    private int errorCode;
    private String message;
}
