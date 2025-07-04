package com.MenuMaker.MenuMakerApi.model.menuData.dish;

import com.MenuMaker.MenuMakerApi.model.menuData.dish.priceValidation.Price;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// TODO create validation for field
public record Dish(
    @NotBlank @Size(max = 30) String name,
    @Price String price,
    @NotBlank @Size(max = 75) String description
    ) {

}
