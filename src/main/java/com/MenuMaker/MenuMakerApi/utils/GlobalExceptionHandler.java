package com.MenuMaker.MenuMakerApi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> unhandledException(Exception e) {
        log.error("Server error: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ApiResponse> missingCookie(MissingRequestCookieException e) {
        log.error("Missing cookie in request: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", null);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> jwtException(JwtException e) {
        log.error("JwtException: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", null);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse> signatureJwtException(SignatureException e) {
        log.error("Jwt signature error: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", null);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse> validationException(ValidationException e) {
        log.error("Error in field validation: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.BAD_REQUEST, "Invalid Fields", null);
    }
}
