package com.MenuMaker.MenuMakerApi.model.response;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.MenuMaker.MenuMakerApi.model.abstractClass.AbstractMenuData;
import com.MenuMaker.MenuMakerApi.model.menuData.Dish;
import com.MenuMaker.MenuMakerApi.model.menuData.Style;

public class UserMenuResponse extends AbstractMenuData {

    public UserMenuResponse(Date creationDate, Style style, Map<String, List<Dish>> content) {
        super.setCreationDate(creationDate);
        super.setStyle(style);
        super.setContent(content);
    }
}
