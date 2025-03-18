package com.MenuMaker.MenuMakerApi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
            log.debug("Login from {}", loginRequest.getEmail());
            // create a token with 10min expiration time with email

            // then send email with short expire time

            return ResponseUtils.buildResponse(HttpStatus.OK, "Email sent", null);
        } catch (ValidationException e) {
            log.error("Error in login validation: {}", e.getMessage());
            return ResponseUtils.buildResponse(HttpStatus.BAD_REQUEST, "Invalid Fields", null);
        } catch (Exception e) {
            log.error("Error in login: {}", e.getMessage());
            return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null);
        }
    }

    @GetMapping("/auth-token")
    public ResponseEntity<ApiResponse> getAuthToken(@RequestHeader("Authorization") String token) {
        try {
            // validate the token

            // get email from token then check if he's in DB

            // if not created then is created

            // send a cookie with the token on httpOnly
            // redirection to the dashbaord ?
            return ResponseUtils.buildResponse(HttpStatus.OK, "Successfully authenticate", null);
        } catch (Exception e) {
            return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null);
        }
    }
}
