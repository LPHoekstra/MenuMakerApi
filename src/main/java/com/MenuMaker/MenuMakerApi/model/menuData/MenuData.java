package com.MenuMaker.MenuMakerApi.model.menuData;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.MenuMaker.MenuMakerApi.model.abstractClass.AbstractMenuData;

public class MenuData extends AbstractMenuData {

    public MenuData(Date creationDate, Style style, Map<String, List<Dish>> content) {
        super(creationDate, style, content);
    }
}
