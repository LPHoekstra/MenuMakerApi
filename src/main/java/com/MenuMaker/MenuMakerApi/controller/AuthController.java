package com.MenuMaker.MenuMakerApi.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MenuMaker.MenuMakerApi.model.request.LoginRequest;
import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;
import com.MenuMaker.MenuMakerApi.service.AuthService;
import com.MenuMaker.MenuMakerApi.service.EmailService;
import com.MenuMaker.MenuMakerApi.service.TokenBlacklistService;
import com.MenuMaker.MenuMakerApi.service.TokenService;
import com.MenuMaker.MenuMakerApi.utils.ResponseUtils;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${frontendDomain}")
    private String frontendDomain;

    @Value("${backendDomain}")
    private String backendDomain;

    private final AuthService authService;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(AuthService authService, EmailService emailService, TokenService tokenService,
            TokenBlacklistService tokenBlacklistService) {
        this.authService = authService;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws MessagingException {
        log.debug("Login from {}", loginRequest.getEmail());

        String token = tokenService.shortTimeToken(loginRequest.getEmail());
        String link = backendDomain + "/api/v1/auth/login/" + token;

        emailService.sendMagicLink(loginRequest.getEmail(), link);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Email sent", null);
    }

    @GetMapping("/login/{token}")
    public ResponseEntity<ApiResponse> getAuthToken(@PathVariable("token") String token, HttpServletResponse response)
            throws IOException {
        try {
            log.debug("Authentification with short time token {}", token);

            String email = tokenService.getEmailFromToken(token);
            authService.checkEmailIsRegistered(email);

            String longTimeToken = tokenService.longTimeToken(email);

            authService.createAuthCookie(response, longTimeToken);
            response.sendRedirect(frontendDomain + "/dashboard");

            return ResponseUtils.buildResponse(HttpStatus.MOVED_PERMANENTLY, "Successfully authenticate", null,
                    response);
        } catch (Exception e) {
            log.error("Error sending auth token {}", e);
            response.sendRedirect(frontendDomain);
            return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null,
                    response);
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@CookieValue("authToken") String authToken,
            HttpServletResponse response) {
        log.debug("logout token {}", authToken);

        tokenBlacklistService.addTokenToBlacklist(authToken);

        authService.deleteAuthCookie(response);

        return ResponseUtils.buildResponse(HttpStatus.OK, "Successfully disconnected", null);
    }
}
