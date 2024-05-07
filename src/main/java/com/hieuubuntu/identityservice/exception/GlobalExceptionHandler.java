package com.hieuubuntu.identityservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hieuubuntu.identityservice.dto.response.DefaultResponse;
import com.hieuubuntu.identityservice.exception.error_code.ErrorCode;
import com.hieuubuntu.identityservice.exception.type.AppException;
import com.hieuubuntu.identityservice.exception.type.CanPermissionException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    // Normal:
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<DefaultResponse> handleAllExceptions(RuntimeException e) {
        log.error("RuntimeException:", e.toString());
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

        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<DefaultResponse> handleAppExceptions(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        DefaultResponse response = new DefaultResponse();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<DefaultResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        DefaultResponse response = new DefaultResponse();
        response.setSuccess(false);
        response.setCode(ErrorCode.INVALID_PARAMS_REQUEST.getCode());

        String messageError = ErrorCode.DEFAULT_ERROR.getMessage();
        FieldError fieldError = e.getFieldError();
        if (fieldError != null) {
            messageError = fieldError.getDefaultMessage();
        }
        response.setMessage(messageError);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    ResponseEntity<DefaultResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        DefaultResponse response = new DefaultResponse();
        response.setSuccess(false);
        response.setCode(ErrorCode.INVALID_PARAMS_REQUEST.getCode());
        response.setMessage(e.getParameterName() + " is required");
        return ResponseEntity.badRequest().body(response);
    }

    // CanPer

    // CanPer
    @ExceptionHandler(value = CanPermissionException.class)
    ResponseEntity<DefaultResponse> handleMethodValidationException(CanPermissionException e) {
        log.error("CanPermissionException:", e.toString());
        DefaultResponse response = new DefaultResponse();
        response.setSuccess(false);
        String message = e.getMessage();
        response.setMessage(message);
        response.setCode(ErrorCode.NOT_PERMISSION.getCode());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
