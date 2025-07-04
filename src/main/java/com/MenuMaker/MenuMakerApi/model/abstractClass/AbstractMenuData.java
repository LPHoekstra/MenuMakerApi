package com.MenuMaker.MenuMakerApi.model.abstractClass;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.MenuMaker.MenuMakerApi.model.menuData.dish.Dish;
import com.MenuMaker.MenuMakerApi.model.menuData.style.Style;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public abstract class AbstractMenuData {
    @NotNull
    private Date creationDate;
    @NotNull
    private Style style;
    @NotEmpty
    private Map<String, List<Dish>> content;

    public AbstractMenuData() {
    }

    public AbstractMenuData(Date creationDate, Style style, Map<String, List<Dish>> content) {
        this.creationDate = creationDate;
        this.style = style;
        this.content = content;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

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
