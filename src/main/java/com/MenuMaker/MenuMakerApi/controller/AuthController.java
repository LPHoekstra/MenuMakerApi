package com.MenuMaker.MenuMakerApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MenuMaker.MenuMakerApi.model.request.LoginRequest;
import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;
import com.MenuMaker.MenuMakerApi.service.AuthService;
import com.MenuMaker.MenuMakerApi.service.EmailService;
import com.MenuMaker.MenuMakerApi.utils.ResponseUtils;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @Autowired
    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.debug("Login from {}", loginRequest.getEmail());
            // create a token with 10min expiration time with email
            String token = authService.shortTimeToken(loginRequest.getEmail());
            String link = "http://localhost:3001/api/v1/auth/login?token=" + token;

            // then send email with link
            emailService.sendMagicLink(loginRequest.getEmail(), link);

            return ResponseUtils.buildResponse(HttpStatus.OK, "Email sent", null);

        } catch (ValidationException e) {
            log.error("Error in login validation: {}", e.getMessage());
            return ResponseUtils.buildResponse(HttpStatus.BAD_REQUEST, "Invalid Fields", null);

        } catch (MessagingException e) {
            log.error("Error while sending email: {}", e.getMessage());
            return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error while email sending", null);

        } catch (Exception e) {
            log.error("Error in login: {}", e.getMessage());
            return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<ApiResponse> getAuthToken(@RequestParam("token") String token) {
        try {
            // return string email: get email from token
            String email = authService.getEmailFromToken(token);

            // return boolean: check if he's in DB
            boolean isInDB = authService.isEmailRegistered(email);

            if (!isInDB) {
                // if not created then he's created
            }

            // return string token: create a token of 6 hours validation with email

            // in response =>
            // send token in a httpOnly cookie
            // redirection to the dashbaord ?
            return ResponseUtils.buildResponse(HttpStatus.OK, "Successfully authenticate", null);
        } catch (Exception e) {
            return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null);
        }
    }
}
