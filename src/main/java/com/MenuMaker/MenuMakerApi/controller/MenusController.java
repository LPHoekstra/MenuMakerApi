package com.MenuMaker.MenuMakerApi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MenuMaker.MenuMakerApi.exception.TokenBlacklistedException;
import com.MenuMaker.MenuMakerApi.model.request.CreateMenuRequest;
import com.MenuMaker.MenuMakerApi.model.request.PutMenuRequest;
import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;
import com.MenuMaker.MenuMakerApi.model.response.UserMenuResponse;
import com.MenuMaker.MenuMakerApi.model.response.GetUserMenusResponse;
import com.MenuMaker.MenuMakerApi.service.MenuService;
import com.MenuMaker.MenuMakerApi.service.TokenBlacklistService;
import com.MenuMaker.MenuMakerApi.service.TokenService;
import com.MenuMaker.MenuMakerApi.utils.ResponseUtils;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/menus")
public class MenusController {
    private String userEmail;

    private final MenuService menuService;
    private final TokenService tokenService;
    private final TokenBlacklistService tokenBlacklistService;

    MenusController(MenuService menuService, TokenService tokenService, TokenBlacklistService tokenBlacklistService) {
        this.menuService = menuService;
        this.tokenService = tokenService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    /**
     * Check the auth token from user
     * @param authToken
     */
    @ModelAttribute("/")
    public void verifyAuthToken(@CookieValue("authToken") String authToken) {
        if (tokenBlacklistService.isTokenBlacklisted(authToken)) {
            throw new TokenBlacklistedException("Token is blacklisted");
        }
        log.debug("verify token");

        this.userEmail = tokenService.getEmailFromToken(authToken);
    }

    @GetMapping("/userMenus")
    public ResponseEntity<ApiResponse> getUserMenus() {
        log.debug("Getting menu with token: {}");

        List<GetUserMenusResponse> userMenusList = menuService.getMenus(this.userEmail);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Menus retrieved successfully", userMenusList);
    }

    @PostMapping("/createMenu")
    public ResponseEntity<ApiResponse> createMenu(@Valid @RequestBody CreateMenuRequest createMenuRequest) {
        log.debug("Create a menu: {}", createMenuRequest);

        menuService.saveMenu(createMenuRequest, this.userEmail);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Menu created successfully");
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse> getUserMenu(@PathVariable("menuId") String menuId) {
        log.debug("Getting menu with id: {}", menuId);

        UserMenuResponse userMenuResponse = menuService.getMenu(menuId, this.userEmail);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Menu retrieved successfully", userMenuResponse);
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse> putUserMenu(@PathVariable("menuId") String menuId,
            @RequestBody PutMenuRequest putMenuRequest) {
        log.debug("Put menu with id: {}", menuId);

        menuService.putMenu(menuId, this.userEmail, putMenuRequest);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Menu update successfully");
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponse> deleteUserMenu(@PathVariable("menuId") String menuId) {
        log.debug("Deleting menu with id: {}", menuId);

        menuService.deleteMenu(menuId, this.userEmail);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Menu deleted successfully");
    }
}
