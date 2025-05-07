package com.MenuMaker.MenuMakerApi.model.menuData;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class MenuData {
    private Date creationDate;
    private Style style;
    private Map<String, List<Dish>> content;

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
