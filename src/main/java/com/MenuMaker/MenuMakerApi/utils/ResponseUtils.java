package com.MenuMaker.MenuMakerApi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.MenuMaker.MenuMakerApi.model.response.ApiResponse;

public class ResponseUtils {

    public static ResponseEntity<ApiResponse> buildResponse(
            HttpStatus httpStatus,
            String message,
            Object data) {
        return ResponseEntity.status(httpStatus)
                .body(new ApiResponse(httpStatus.value(), message, data));
    }
}
