package com.MenuMaker.MenuMakerApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MenuMaker.MenuMakerApi.model.request.LoginRequest;
import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;
import com.MenuMaker.MenuMakerApi.service.AuthService;
import com.MenuMaker.MenuMakerApi.service.EmailService;
import com.MenuMaker.MenuMakerApi.utils.ResponseUtils;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
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

    // TODO: what if the token is expired, no redirection or message ?
    @GetMapping("/login")
    public ResponseEntity<ApiResponse> getAuthToken(@RequestParam("token") String token, HttpServletResponse response) {
        try {
            log.debug("Authentification with short time token {}", token);

            String email = authService.getEmailFromToken(token);
            boolean isEmailInDB = authService.isEmailRegistered(email);

            if (!isEmailInDB) {
                authService.registerUser(email);
            }

            String longTimeToken = authService.longTimeToken(email);

            authService.createAuthCookie(response, longTimeToken);
            response.sendRedirect("http://localhost:5173/dashboard");

            return ResponseUtils.buildResponse(HttpStatus.MOVED_PERMANENTLY, "Successfully authenticate", null,
                    response);
        } catch (Exception e) {
            log.error("Error sending auth token {}", e);
            // set a redirection to home page
            return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null);
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Cookie") String authTokens,
            HttpServletResponse response) {
        try {
            log.debug("logout token {}", authTokens);

            // implement blacklist for token
            String token = authTokens.split(";")[0].split("=")[1];
            log.info("token blacklisted {}", token);

            authService.deleteAuthCookie(response);

            return ResponseUtils.buildResponse(HttpStatus.OK, "Successfully disconnected", null);
        } catch (Exception e) {
            log.error("Error during logout {}", e);
            return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null);
        }
    }
}
