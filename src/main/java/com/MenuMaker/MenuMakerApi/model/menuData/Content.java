package com.MenuMaker.MenuMakerApi.model.menuData;

import java.util.List;
import java.util.Map;

public class Content {
    private Map<String, List<Dish>> category;

    public Object getCategory() {
        return this.category;
    }

    public void setCategory(Map<String, List<Dish>> category) {
        this.category = category;
    }
}
