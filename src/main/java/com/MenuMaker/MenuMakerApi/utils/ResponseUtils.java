package com.MenuMaker.MenuMakerApi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class ResponseUtils {

    public static ResponseEntity<ApiResponse> buildResponse(
            HttpStatus httpStatus,
            String message,
            Object data,
            HttpServletResponse response,
            Cookie cookie) {

        if (cookie != null) {
            response.addCookie(cookie);
        }

        return ResponseEntity.status(httpStatus)
                .body(new ApiResponse(httpStatus.value(), message, data));
    }

    public static ResponseEntity<ApiResponse> buildResponse(
            HttpStatus httpStatus,
            String message,
            Object data) {
        return ResponseEntity.status(httpStatus)
                .body(new ApiResponse(httpStatus.value(), message, data));
    }
}
