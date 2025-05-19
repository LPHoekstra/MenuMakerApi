package com.MenuMaker.MenuMakerApi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MenuMaker.MenuMakerApi.model.request.CreateMenuRequest;
import com.MenuMaker.MenuMakerApi.model.request.PutMenuRequest;
import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;
import com.MenuMaker.MenuMakerApi.model.response.UserMenuResponse;
import com.MenuMaker.MenuMakerApi.model.response.UserMenusResponse;
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

    private final MenuService menuService;
    private final TokenService tokenService;
    private final TokenBlacklistService tokenBlacklistService;

    MenusController(MenuService menuService, TokenService tokenService, TokenBlacklistService tokenBlacklistService) {
        this.menuService = menuService;
        this.tokenService = tokenService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/createMenu")
    public ResponseEntity<ApiResponse> createMenu(@CookieValue("authToken") String authToken,
            @Valid @RequestBody CreateMenuRequest createMenuRequest) {
        log.debug("Create a menu: {}", createMenuRequest);

        String userEmail = getEmailAndCheckToken(authToken);

        menuService.saveMenu(createMenuRequest, userEmail);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Menu created successfully", null);
    }

    @GetMapping("/userMenus")
    public ResponseEntity<ApiResponse> getUserMenus(@CookieValue("authToken") String authToken) {
        log.debug("Getting menu with token: {}", authToken);

        String userEmail = getEmailAndCheckToken(authToken);
        List<UserMenusResponse> userMenusList = menuService.getMenusDatas(userEmail);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Menus retrieved successfully", userMenusList);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse> getUserMenu(@PathVariable("menuId") String menuId,
            @CookieValue("authToken") String authToken) {
        log.debug("Getting menu with id: {}", menuId);

        String userEmail = getEmailAndCheckToken(authToken);
        UserMenuResponse userMenuResponse = menuService.getMenu(menuId, userEmail);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Menu retrieved successfully", userMenuResponse);
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse> putUserMenu(@PathVariable("menuId") String menuId,
            @CookieValue("authToken") String authToken,
            @RequestBody PutMenuRequest putMenuRequest) {
        log.debug("Put menu with id: {}", menuId);

        String userEmail = getEmailAndCheckToken(authToken);
        menuService.putMenu(menuId, userEmail, putMenuRequest);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Menu update successfully", null);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResponse> deleteUserMenu(@PathVariable("menuId") String menuId,
            @CookieValue("authToken") String authToken) {
        log.debug("Deleting menu with id: {}", menuId);

        String userEmail = getEmailAndCheckToken(authToken);
        menuService.deleteMenu(menuId, userEmail);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Menu deleted successfully", null);
    }

    private String getEmailAndCheckToken(String authToken) {
        if (tokenBlacklistService.isTokenBlacklisted(authToken)) {
            throw new AccessDeniedException("Token is blacklisted");
        }
        return tokenService.getEmailFromToken(authToken);
    }
}
