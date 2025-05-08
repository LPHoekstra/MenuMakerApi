package com.MenuMaker.MenuMakerApi.service;

import org.springframework.stereotype.Service;

import com.MenuMaker.MenuMakerApi.model.MenuDataModel;
import com.MenuMaker.MenuMakerApi.model.request.CreateMenuRequest;
import com.MenuMaker.MenuMakerApi.repository.MenuRepository;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * 
     * @param createMenuRequest
     * @param userEmail
     * @return the saved entity; will never be null.
     * @throws IllegalArgumentException - in case the given entity is null.
     */
    public MenuDataModel saveMenu(CreateMenuRequest createMenuRequest, String userEmail) {
        MenuDataModel menuToSave = new MenuDataModel(
                userEmail,
                createMenuRequest);

        return menuRepository.save(menuToSave);
    }
}
