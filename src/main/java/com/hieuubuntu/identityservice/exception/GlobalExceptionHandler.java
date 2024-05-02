package com.hieuubuntu.identityservice.exception;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.hieuubuntu.identityservice.dto.response.DefaultResponse;
import com.hieuubuntu.identityservice.exception.error_code.ErrorCode;
import com.hieuubuntu.identityservice.exception.type.AppException;
import com.hieuubuntu.identityservice.permissions.CanPer;

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
    @ExceptionHandler(value = HandlerMethodValidationException.class)
    ResponseEntity<DefaultResponse> handleMethodValidationException(HandlerMethodValidationException e) {
        log.error("HandlerMethodValidationException:", e.toString());
        DefaultResponse response = new DefaultResponse();
        response.setSuccess(false);
        int code = ErrorCode.DEFAULT_ERROR.getCode();
        String message = ErrorCode.DEFAULT_ERROR.getMessage();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        // Xử lý exception không có quyền:
        CanPer canPer = e.getMethod().getAnnotation(CanPer.class);
        if (Objects.nonNull(canPer)) {
            ErrorCode errorCode = ErrorCode.valueOf(canPer.message());
            code = errorCode.getCode();
            message = mapAttribute(errorCode.getMessage(), canPer.name());
            httpStatus = HttpStatus.FORBIDDEN;
        }

        response.setMessage(message);
        response.setCode(code);
        return ResponseEntity.status(httpStatus).body(response);
    }

    private String mapAttribute(String message, String attribute) {
        return message.replace("{name}", attribute);
    }
}
