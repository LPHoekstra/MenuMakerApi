package com.MenuMaker.MenuMakerApi.serviceTest.authService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.MenuMaker.MenuMakerApi.service.AuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class ShortTimeTokenTest {
    private SecretKey secretKey;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        String secret = "vE5v2pd7VWFA8DOhDBO+5Ecirg95kcs+14+iWUFP0S4=";
        authService = new AuthService(secret);
        secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    @Test
    void generateTokenWithShortExpiration() throws Exception {
        String userEmail = "test@gmail.com";

        // act
        String token = authService.shortTimeToken(userEmail);

        assertNotNull(token, "Token must not be null");

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(userEmail, claims.getSubject(), "Expect token payload to be userEmail");

        long expiration = claims.getExpiration().getTime();
        long expectedExpiration = claims.getIssuedAt().getTime() + (10 * 60 * 1000);

        assertEquals(expectedExpiration, expiration, "Token must have a 10min expiration");
    }
}
