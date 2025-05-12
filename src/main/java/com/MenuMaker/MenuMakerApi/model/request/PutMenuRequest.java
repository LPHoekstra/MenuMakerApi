package com.MenuMaker.MenuMakerApi.model.request;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.MenuMaker.MenuMakerApi.model.abstractClass.AbstractMenuData;
import com.MenuMaker.MenuMakerApi.model.menuData.Dish;
import com.MenuMaker.MenuMakerApi.model.menuData.Style;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PutMenuRequest extends AbstractMenuData {

    @Override
    @NotNull
    public Date getCreationDate() {
        return super.getCreationDate();
    }

    @Override
    @NotNull
    public Style getStyle() {
        return super.getStyle();
    }

    @Override
    @NotEmpty
    public Map<String, List<Dish>> getContent() {
        return super.getContent();
    }
}
