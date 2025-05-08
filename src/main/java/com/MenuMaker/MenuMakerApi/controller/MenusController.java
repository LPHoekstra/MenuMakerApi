package com.MenuMaker.MenuMakerApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MenuMaker.MenuMakerApi.model.request.CreateMenuRequest;
import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;
import com.MenuMaker.MenuMakerApi.service.AuthService;
import com.MenuMaker.MenuMakerApi.service.MenuService;
import com.MenuMaker.MenuMakerApi.utils.ResponseUtils;

import jakarta.validation.Valid;
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

    // need field validation in createMenuRequest
    @PostMapping("/createMenu")
    public ResponseEntity<ApiResponse> createMenu(@CookieValue("authToken") String authToken,
            @Valid @RequestBody CreateMenuRequest createMenuRequest) {
        log.debug("Create a menu: {}", createMenuRequest);

        String userEmail = authService.getEmailFromToken(authToken);

        menuService.saveMenu(createMenuRequest, userEmail);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Menu created successfully", null);
    }
}
