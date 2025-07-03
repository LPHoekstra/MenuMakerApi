package com.MenuMaker.MenuMakerApi.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;
import com.MenuMaker.MenuMakerApi.utils.ResponseUtils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> unhandledException(Exception e) {
        log.error("Server error: {}", e);
        return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ApiResponse> missingCookie(MissingRequestCookieException e) {
        log.error("Missing cookie in request: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> jwtException(JwtException e) {
        log.error("JwtException: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse> signatureJwtException(SignatureException e) {
        log.error("Jwt signature error: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse> validationException(ValidationException e) {
        log.error("Error in field validation: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.BAD_REQUEST, "Invalid Fields");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> nullParamException(IllegalArgumentException e) {
        log.error("Given entity is null: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.BAD_REQUEST, "Bad Request");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> entityNotFoundException(EntityNotFoundException e) {
        log.error("Given entity not found: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.NOT_FOUND, "Not found");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> accessDeniedException(AccessDeniedException e) {
        log.error("Access denied: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.FORBIDDEN, "Access denied");
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ApiResponse> messagingException(MessagingException e) {
        log.error("Error while sending email: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error while email sending");
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse> ioException(IOException e) {
        log.error("Error during an input or output operations: {}", e.getMessage());
        return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Interal Server Error");
    } 
}
