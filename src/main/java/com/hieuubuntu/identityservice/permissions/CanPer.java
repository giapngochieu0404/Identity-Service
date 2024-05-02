package com.hieuubuntu.identityservice.permissions;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = { CanPermission.class }
)
public @interface CanPer {
    String message() default "Bạn không có quyền truy cập tính năng này";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String name();
}
