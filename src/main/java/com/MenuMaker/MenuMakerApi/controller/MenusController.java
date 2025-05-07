package com.MenuMaker.MenuMakerApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MenuMaker.MenuMakerApi.model.MenuDataModel;
import com.MenuMaker.MenuMakerApi.model.request.CreateMenuRequest;
import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;
import com.MenuMaker.MenuMakerApi.service.AuthService;
import com.MenuMaker.MenuMakerApi.service.MenuService;
import com.MenuMaker.MenuMakerApi.utils.Function;
import com.MenuMaker.MenuMakerApi.utils.ResponseUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/menus")
public class MenusController {

    private final MenuService menuService;
    private final AuthService authService;

    MenusController(MenuService menuService, AuthService authService) {
        this.menuService = menuService;
        this.authService = authService;
    }

    // need to sent a response for 400 and 401
    @PostMapping("/createMenu")
    public ResponseEntity<ApiResponse> createMenu(@RequestHeader("Cookie") String cookie,
            @RequestBody CreateMenuRequest createMenuRequest) {
        try {
            log.debug("Create a menu: {}", createMenuRequest);

            String authToken = Function.getCookie(cookie, "authToken");
            if (authToken == null) {
                throw new Exception("Error retrieving the authToken cookie");
            }
            String userEmail = authService.getEmailFromToken(authToken);

            MenuDataModel savedMenu = menuService.saveMenu(createMenuRequest, userEmail);

            if (savedMenu == null) {
                throw new Exception("Error during the save of the menu");
            }

            return ResponseUtils.buildResponse(HttpStatus.OK, "Menu created successfully", null);
        } catch (Exception e) {
            log.error("Error in create menu: {}", e.getMessage());
            return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null);
        }
    }
}
