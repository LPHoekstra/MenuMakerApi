package com.MenuMaker.MenuMakerApi.model.menuData.style.colorValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ColorValidator implements ConstraintValidator<Color, String> {
    private final String REGEX = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.matches(REGEX)) {
            return true;
        }
        
        return false;
    }
}
