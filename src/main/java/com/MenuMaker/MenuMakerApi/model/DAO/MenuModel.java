package com.MenuMaker.MenuMakerApi.model.DAO;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.MenuMaker.MenuMakerApi.model.menuData.MenuData;

@Document(collection = "menus")
public class MenuModel {
    @Id
    private String id;
    private String userEmail;
    private MenuData menuData;

    public MenuModel() {
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
