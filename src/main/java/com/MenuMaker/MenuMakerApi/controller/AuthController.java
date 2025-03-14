package com.MenuMaker.MenuMakerApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MenuMaker.MenuMakerApi.model.request.LoginRequest;
import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;
import com.MenuMaker.MenuMakerApi.utils.ResponseUtils;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {

            return ResponseUtils.buildResponse(HttpStatus.OK, "Email sent", null);
        } catch (ValidationException e) {
            return ResponseUtils.buildResponse(HttpStatus.BAD_REQUEST, "Invalid Fields", null);
        } catch (Exception e) {
            return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null);
        }

    }
}
