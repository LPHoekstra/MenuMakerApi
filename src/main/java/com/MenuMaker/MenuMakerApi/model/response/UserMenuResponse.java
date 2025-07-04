package com.MenuMaker.MenuMakerApi.model.response;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.MenuMaker.MenuMakerApi.model.abstractClass.AbstractMenuData;
import com.MenuMaker.MenuMakerApi.model.menuData.dish.Dish;
import com.MenuMaker.MenuMakerApi.model.menuData.style.Style;

public class UserMenuResponse extends AbstractMenuData {

    public UserMenuResponse(Date creationDate, Style style, Map<String, List<Dish>> content) {
        super(creationDate, style, content);
    }
}
