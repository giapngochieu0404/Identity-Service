package com.hieuubuntu.identityservice.annotations;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CanPer {
    String message() default "NOT_PERMISSION";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String name();
}
