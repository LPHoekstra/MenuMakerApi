package com.MenuMaker.MenuMakerApi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.MenuMaker.MenuMakerApi.model.MenuModel;
import com.MenuMaker.MenuMakerApi.model.menuData.MenuData;
import com.MenuMaker.MenuMakerApi.model.request.CreateMenuRequest;
import com.MenuMaker.MenuMakerApi.model.response.UserMenusResponse;
import com.MenuMaker.MenuMakerApi.repository.MenuRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * 
     * @param menuData
     * @param userEmail
     * @return the saved entity; will never be null.
     * @throws IllegalArgumentException - in case the given entity is null.
     */
    public MenuModel saveMenu(CreateMenuRequest createMenuRequest, String userEmail) {
        MenuData menuData = new MenuData();
        menuData.setCreationDate(createMenuRequest.getCreationDate());
        menuData.setStyle(createMenuRequest.getStyle());
        menuData.setContent(createMenuRequest.getContent());

        MenuModel menuToSave = new MenuModel();
        menuToSave.setUserEmail(userEmail);
        menuToSave.setMenuData(menuData);

        return menuRepository.save(menuToSave);
    }

    /**
     * Get all menus created by the user with their email.
     * 
     * @param userEmail
     * @return an array of {@link UserMenusResponse}
     */
    public List<UserMenusResponse> getMenusDatas(String userEmail) {
        List<MenuModel> arrayMenuModel = menuRepository.findAllByUserEmail(userEmail);

        List<UserMenusResponse> arrayToSend = new ArrayList<>();

        for (MenuModel menuModel : arrayMenuModel) {
            String id = menuModel.getId();
            Date creationDate = menuModel.getMenuData().getCreationDate();

            UserMenusResponse menuResponse = new UserMenusResponse(id, creationDate);
            arrayToSend.add(menuResponse);
        }

        return arrayToSend;
    }

    /**
     * Delete a specific menu with his id
     * 
     * @param menuId id of the menu
     * @throws IllegalArgumentException - in case the given id is null.
     */
    public void deleteMenu(String menuId) {
        menuRepository.deleteById(menuId);
    }
}
