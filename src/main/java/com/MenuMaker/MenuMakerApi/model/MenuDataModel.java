package com.MenuMaker.MenuMakerApi.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.MenuMaker.MenuMakerApi.model.menuData.Style;

@Document(collection = "menuData")
public class MenuDataModel {
    @Id
    private String id;
    private Style style;
    private Object content;
    private Date creationDate;

    MenuDataModel() {
        this.creationDate = new Date();
    }

    public String getId() {
        return this.id;
    }

    public Style getStyle() {
        return this.style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Object getContent() {
        return this.content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
