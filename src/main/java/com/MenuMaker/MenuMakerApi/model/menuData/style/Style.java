package com.MenuMaker.MenuMakerApi.model.menuData.style;

import com.MenuMaker.MenuMakerApi.model.menuData.style.colorValidation.Color;
import com.MenuMaker.MenuMakerApi.model.menuData.style.fontFamilyValidation.FontFamily;

import jakarta.validation.constraints.NotNull;

public class Style {
    @NotNull
    @Color
    private String color;
    @NotNull
    @FontFamily
    private String fontFamily;

    public Style(String color, String fontFamily) {
        this.color = color;
        this.fontFamily = fontFamily;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
}
