package com.MenuMaker.MenuMakerApi.model.menuData.style.colorValidation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ColorValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Color {
    String message() default "A color must be in format: '#FFFFFF'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
