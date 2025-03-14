package com.MenuMaker.MenuMakerApi.model.response;

public class ApiResponse {
    private int httpStatus;
    private String message;
    private Object data;

    public ApiResponse(int httpStatus, String message, Object data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return this.httpStatus;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }
}
