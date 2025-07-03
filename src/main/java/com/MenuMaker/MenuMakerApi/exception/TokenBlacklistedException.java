package com.MenuMaker.MenuMakerApi.exception;

public class TokenBlacklistedException extends RuntimeException {
    
    public TokenBlacklistedException(String msg) {
        super(msg);
    }
}
