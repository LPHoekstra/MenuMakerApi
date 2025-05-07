package com.MenuMaker.MenuMakerApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MenuMaker.MenuMakerApi.model.MenuDataModel;
import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;
import com.MenuMaker.MenuMakerApi.utils.ResponseUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/menus")
public class MenusController {

    @PostMapping("/createMenu")
    public ResponseEntity<ApiResponse> createMenu(@RequestBody MenuDataModel menuData) {
        try {
            return ResponseUtils.buildResponse(HttpStatus.OK, "Menu created successfully", null);
        } catch (Exception e) {
            return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null);
        }
    }
}
