package com.MenuMaker.MenuMakerApi.service;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenService {
    private final SecretKey secretKey;

    public TokenService(@Value("${secretKey}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    /**
     * 
     * @param email to put in the token
     * @return the token with an expiration of 10min
     */
    public String createShortTimeToken(String email) {
        log.debug("Creating a short time token for email: {}", email);
        final Date expirationDate = new Date(new Date().getTime() + 10 * 60 * 1000L);

        return createToken(email, expirationDate);
    }

    /**
     * 
     * @param email to put in the token
     * @return the token with an expiration of 6 hours
     */
    public String createLongTimeToken(String email) {
        log.debug("Creating a 6 hours expiration token for email: {}", email);
        final Date expirationDate = new Date(new Date().getTime() + 6 * 60 * 60 * 1000L);

        return createToken(email, expirationDate);
    }

    /**
     * Get the email stored in the JWT
     * @param token to get the email from
     * @return the email
     */
    public String getEmailFromToken(String token) {
        log.debug("Getting email from token: {}", token);

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * 
     * @param email from user to put in the token
     * @param expirationDate of the token
     * @return the token
     */
    private String createToken(String email, Date expirationDate) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }
}
