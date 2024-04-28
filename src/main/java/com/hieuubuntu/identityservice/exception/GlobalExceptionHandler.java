package com.hieuubuntu.identityservice.exception;

import com.hieuubuntu.identityservice.dto.response.DefaultResponse;
import com.hieuubuntu.identityservice.exception.error_code.ErrorCode;
import com.hieuubuntu.identityservice.exception.type.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler{
    // Normal:
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<DefaultResponse> handleAllExceptions(Exception e) {
        DefaultResponse response = new DefaultResponse();
        response.setSuccess(false);
        response.setCode(ErrorCode.DEFAULT_ERROR.getCode());
        response.setMessage(ErrorCode.DEFAULT_ERROR.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<DefaultResponse> handleAppExceptions(AppException e) {
        ErrorCode errorCode = e.getErrorCode();

        DefaultResponse response = new DefaultResponse();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<DefaultResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        DefaultResponse response = new DefaultResponse();
        response.setSuccess(false);
        response.setCode(ErrorCode.INVALID_PARAMS_REQUEST.getCode());

        String messageError = "Lỗi không xác định";
        FieldError fieldError = e.getFieldError();
        if (fieldError != null) {
            messageError = fieldError.getDefaultMessage();
        }
        response.setMessage(messageError);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    ResponseEntity<DefaultResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        DefaultResponse response = new DefaultResponse();
        response.setSuccess(false);
        response.setCode(ErrorCode.INVALID_PARAMS_REQUEST.getCode());
        response.setMessage(e.getParameterName() + " is required");
        return ResponseEntity.badRequest().body(response);
    }

}
