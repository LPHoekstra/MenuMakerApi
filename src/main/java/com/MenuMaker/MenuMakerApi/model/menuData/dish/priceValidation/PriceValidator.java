package com.MenuMaker.MenuMakerApi.model.menuData.dish.priceValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<Price, String> {
    private final String REGEX = "^[0-9].€$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.matches(REGEX)) {
            return true;
        }
        
        return false;
    }
    
}
