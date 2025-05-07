package com.MenuMaker.MenuMakerApi.model.request;

import java.util.List;
import java.util.Map;

import com.MenuMaker.MenuMakerApi.model.menuData.Dish;
import com.MenuMaker.MenuMakerApi.model.menuData.Style;

public class CreateMenuRequest {
    private Style style;
    private Map<String, List<Dish>> content;

    public Style getStyle() {
        return this.style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Map<String, List<Dish>> getContent() {
        return this.content;
    }

    public void setContent(Map<String, List<Dish>> content) {
        this.content = content;
    }
}
