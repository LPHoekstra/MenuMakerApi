package com.MenuMaker.MenuMakerApi.model.menuData;

public class Style {
    private String color; // create a type with only the available color
    private String fontFamily; // create a type with only the available fontFamily

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFontFamily() {
        return this.fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
}
