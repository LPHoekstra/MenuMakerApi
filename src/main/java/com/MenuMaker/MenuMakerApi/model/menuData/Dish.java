package com.MenuMaker.MenuMakerApi.model.menuData;

import jakarta.validation.constraints.NotBlank;

public class Dish {
    @NotBlank
    private String name;
    @NotBlank
    private String price;
    @NotBlank
    private String description;
    // private String img;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // public String getImg() {
    // return this.img;
    // }

    // public void setImg(String img) {
    // this.img = img;
    // }
}
