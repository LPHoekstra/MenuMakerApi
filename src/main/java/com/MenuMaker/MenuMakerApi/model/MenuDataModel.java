package com.MenuMaker.MenuMakerApi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.MenuMaker.MenuMakerApi.model.menuData.MenuData;

@Document(collection = "menuData")
public class MenuDataModel {
    @Id
    private String id;
    private String userEmail;
    private MenuData menuData;

    public MenuDataModel(String userEmail, MenuData menuData) {
        this.userEmail = userEmail;
        this.menuData = menuData;
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

    public MenuData getMenuData() {
        return this.menuData;
    }

    public void setMenuData(MenuData menuData) {
        this.menuData = menuData;
    }
}
