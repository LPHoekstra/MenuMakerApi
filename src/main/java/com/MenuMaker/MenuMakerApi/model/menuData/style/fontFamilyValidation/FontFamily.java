package com.MenuMaker.MenuMakerApi.model.menuData.style.fontFamilyValidation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = FontFamilyValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FontFamily {
    String message() default "Not a valid font family";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
