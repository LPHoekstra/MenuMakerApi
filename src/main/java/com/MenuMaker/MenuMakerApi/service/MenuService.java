package com.MenuMaker.MenuMakerApi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.MenuMaker.MenuMakerApi.model.DAO.MenuModel;
import com.MenuMaker.MenuMakerApi.model.menuData.MenuData;
import com.MenuMaker.MenuMakerApi.model.request.CreateMenuRequest;
import com.MenuMaker.MenuMakerApi.model.request.PutMenuRequest;
import com.MenuMaker.MenuMakerApi.model.response.UserMenuResponse;
import com.MenuMaker.MenuMakerApi.model.response.GetUserMenusResponse;
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
     * Get all menus created by the user with their email.
     * @param userEmail
     * @return an array of {@link GetUserMenusResponse}
     */
    public List<GetUserMenusResponse> getMenus(String userEmail) {
        List<MenuModel> arrayMenuModel = menuRepository.findAllByUserEmail(userEmail);

        // create DTO
        List<GetUserMenusResponse> userMenusResponse = new ArrayList<GetUserMenusResponse>();
        arrayMenuModel.forEach((menuModel) -> {
            String id = menuModel.getId();
            Date creationDate = menuModel.getMenuData().getCreationDate();

            GetUserMenusResponse menuResponse = new GetUserMenusResponse(id, creationDate);
            userMenusResponse.add(menuResponse);
        });

        return userMenusResponse;
    }

    /**
     * Save a menu with a {@link CreateMenuRequest} DTO
     * @param createMenuRequest the DTO
     * @param userEmail from auth token
     * @return the saved entity; will never be null.
     */
    public MenuModel saveMenu(CreateMenuRequest createMenuRequest, String userEmail) {
        MenuData menuData = new MenuData(
            createMenuRequest.getCreationDate(),
            createMenuRequest.getStyle(),
            createMenuRequest.getContent()
        );

        MenuModel menuToSave = new MenuModel();
        menuToSave.setUserEmail(userEmail);
        menuToSave.setMenuData(menuData);

        return menuRepository.save(menuToSave);
    }

    /**
     * Get a specific menu with his id in the DB and return it
     * @param menuId
     * @param userEmail from auth token
     * @return a {@link UserMenuResponse} DTO
     */
    public UserMenuResponse getMenu(String menuId, String userEmail) {
        MenuModel menuOptional = getUserOwnedMenuById(menuId, userEmail);

        MenuData menuData = menuOptional.getMenuData();

        UserMenuResponse userMenuResponse = new UserMenuResponse(
            menuData.getCreationDate(),
            menuData.getStyle(),
            menuData.getContent()
        );

        return userMenuResponse;
    }

    /**
     * Delete a specific menu with its id. Check if the menu is owned by the user email with {@link #getUserOwnedMenuById(String, String)} method.
     * @param menuId id of the menu
     * @param userEmail from auth token
     */
    public void deleteMenu(String menuId, String userEmail) {
        getUserOwnedMenuById(menuId, userEmail);

        menuRepository.deleteById(menuId);
    }

    /**
     * 
     * @param menuId to update
     * @param userEmail from the auth token
     * @param putMenuRequest {@link PutMenuRequest} DTO
     * @return the saved entity; will never be null.
     */
    public MenuModel putMenu(String menuId, String userEmail, PutMenuRequest putMenuRequest) {
        MenuModel menuToUptade = getUserOwnedMenuById(menuId, userEmail);

        menuToUptade.getMenuData().setContent(putMenuRequest.getContent());
        menuToUptade.getMenuData().setStyle(putMenuRequest.getStyle());

        return menuRepository.save(menuToUptade);
    }

    /**
     * Get a menu in the DB by the id
     * @param menuId to retrive
     * @return the {@link MenuModel}
     * @throws EntityNotFoundException if the menu is not found
     */
    private MenuModel getMenuById(String menuId) throws EntityNotFoundException {
        return menuRepository.findById(menuId).
            orElseThrow(() -> new EntityNotFoundException("Menu not find with id: " + menuId)
        );
    }

    /**
     * Get a menu by the id and check if it correspond to the user email.
     * @param menuId to find in the DB
     * @param userEmail from auth token
     * @return the menu from the db
     * @throws EntityNotFoundException if the menu is not found
     * @throws AccessDeniedException if the user email and the mail from the db is not the same
     */
    private MenuModel getUserOwnedMenuById(String menuId, String userEmail) throws EntityNotFoundException, AccessDeniedException {
        MenuModel menuModel = getMenuById(menuId);
        checkMenuOwnership(userEmail, menuModel);

        return menuModel;
    }

    /**
     * Compare the user email and mail from the menu.
     * @param userEmail to compare with the menu.
     * @param menuFromDb get from the DB.
     * @throws AccessDeniedException if the user email and the mail from the db is not the same
     */
    private void checkMenuOwnership(String userEmail, MenuModel menuFromDb) throws AccessDeniedException {
        if (!userEmail.equals(menuFromDb.getUserEmail())) {
            throw new AccessDeniedException("Not authorized to interact with this menu");
        }
    }
}
