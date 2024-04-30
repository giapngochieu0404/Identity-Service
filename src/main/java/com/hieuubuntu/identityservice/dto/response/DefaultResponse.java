package com.hieuubuntu.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponse<T> {
    private int code = 200;
    private String message;
    private boolean success = true;
    private T data;
}
