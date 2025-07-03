package com.MenuMaker.MenuMakerApi.model.menuData.style.fontFamilyValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FontFamilyValidator implements ConstraintValidator<FontFamily, String> {
    private static String[] availableFontFamilies = { "\"Baskervville\", serif", "\"Rubik\", sans-serif", "\"Proza Libre\", sans-serif" };

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for (String fontFamily : availableFontFamilies) {
            if (fontFamily.equals(value)) {
                return true;
            }
        }
        
        return false;
    }
    
}
