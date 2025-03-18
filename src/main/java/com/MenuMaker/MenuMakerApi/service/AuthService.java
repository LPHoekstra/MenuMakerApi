package com.MenuMaker.MenuMakerApi.service;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {
    private SecretKey secretKey;

    @Autowired
    public AuthService(@Value("${secretKey}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    /**
     * 
     * @param userEmail take the email to put in the token
     * @return the token with an expiration of 10min
     */
    public String shortTimeToken(String userEmail) {
        final long JWT_EXPIRATION = 10 * 60 * 1000L;

        return Jwts.builder()
                .subject(userEmail)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + JWT_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }
}
