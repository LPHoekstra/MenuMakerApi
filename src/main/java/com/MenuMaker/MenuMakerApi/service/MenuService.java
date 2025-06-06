package com.MenuMaker.MenuMakerApi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.MenuMaker.MenuMakerApi.model.MenuModel;
import com.MenuMaker.MenuMakerApi.model.menuData.MenuData;
import com.MenuMaker.MenuMakerApi.model.request.CreateMenuRequest;
import com.MenuMaker.MenuMakerApi.model.request.PutMenuRequest;
import com.MenuMaker.MenuMakerApi.model.response.UserMenuResponse;
import com.MenuMaker.MenuMakerApi.model.response.UserMenusResponse;
import com.MenuMaker.MenuMakerApi.repository.MenuRepository;

import jakarta.persistence.EntityNotFoundException;
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
     * Get a specific menu with his id in the DB and return it
     * 
     * @param menuId
     * @param userEmail
     * @return
     */
    public UserMenuResponse getMenu(String menuId, String userEmail) {
        Optional<MenuModel> menuOptional = menuRepository.findById(menuId);

        if (menuOptional.isEmpty()) {
            throw new EntityNotFoundException(menuId);
        }

        checkMenuOwnership(userEmail, menuOptional);

        MenuData menuData = menuOptional.get().getMenuData();

        UserMenuResponse userMenuResponse = new UserMenuResponse(
                menuData.getCreationDate(),
                menuData.getStyle(),
                menuData.getContent());

        return userMenuResponse;
    }

    /**
     * Delete a specific menu with his id
     * 
     * @param menuId    id of the menu
     * @param userEmail got from the auth token to verified if the menu belongs to
     *                  the logged user
     * @throws IllegalArgumentException - in case the given id is null.
     */
    public void deleteMenu(String menuId, String userEmail) {
        Optional<MenuModel> menuToDelete = menuRepository.findById(menuId);

        if (menuToDelete.isEmpty()) {
            throw new EntityNotFoundException(menuId);
        }

        checkMenuOwnership(userEmail, menuToDelete);

        menuRepository.deleteById(menuId);
    }

    /**
     * 
     * @param menuId         to update
     * @param userEmail      got from the auth token
     * @param putMenuRequest body reqest with the menu data
     * @return the saved entity; will never be null.
     */
    public MenuModel putMenu(String menuId, String userEmail, PutMenuRequest putMenuRequest) {
        Optional<MenuModel> menuOptional = menuRepository.findById(menuId);

        if (menuOptional.isEmpty()) {
            throw new EntityNotFoundException("Menu not found");
        }

        checkMenuOwnership(userEmail, menuOptional);

        MenuModel menu = menuOptional.get();
        menu.getMenuData().setContent(putMenuRequest.getContent());
        menu.getMenuData().setStyle(putMenuRequest.getStyle());

        return menuRepository.save(menu);
    }

    private void checkMenuOwnership(String userEmail, Optional<MenuModel> menuFromDb) {
        if (!userEmail.equals(menuFromDb.get().getUserEmail())) {
            throw new AccessDeniedException("Not authorized to interact with this menu");
        }
    }
}
