package com.MenuMaker.MenuMakerApi.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.MenuMaker.MenuMakerApi.model.menuData.Dish;
import com.MenuMaker.MenuMakerApi.model.menuData.Style;

@Document(collection = "menuData")
public class MenuDataModel {
    @Id
    private String id;
    private String userEmail;
    private Style style;
    private Map<String, List<Dish>> content;
    private Date creationDate;

    public MenuDataModel(String userEmail, Style style, Map<String, List<Dish>> content) {
        this.userEmail = userEmail;
        this.style = style;
        this.content = content;
        this.creationDate = new Date();
    }

    public String getId() {
        return this.id;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
