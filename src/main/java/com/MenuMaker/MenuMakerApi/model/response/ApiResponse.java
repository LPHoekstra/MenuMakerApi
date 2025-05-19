package com.MenuMaker.MenuMakerApi.model.response;

public record ApiResponse(int httpStatus, String message, Object data) {

}
