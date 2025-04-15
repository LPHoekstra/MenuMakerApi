package com.MenuMaker.MenuMakerApi.service;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.MenuMaker.MenuMakerApi.model.UserModel;
import com.MenuMaker.MenuMakerApi.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {
    private final SecretKey secretKey;

    private final UserRepository userRepository;

    @Autowired
    public AuthService(@Value("${secretKey}") String secret, UserRepository userRepository) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.userRepository = userRepository;
    }

    /**
     * 
     * @param email to put in the token
     * @return the token with an expiration of 10min
     */
    public String shortTimeToken(String email) {
        log.debug("Creating a short time token for email: {}", email);
        final Date expirationDate = new Date(new Date().getTime() + 10 * 60 * 1000L);

        return createToken(email, expirationDate);
    }

    /**
     * 
     * @param email to put in the token
     * @return the token with an expiration of 6 hours
     */
    public String longTimeToken(String email) {
        log.debug("Creating a 6 hours expiration token for email: {}", email);
        final Date expirationDate = new Date(new Date().getTime() + 6 * 60 * 60 * 1000L);

        return createToken(email, expirationDate);
    }

    /**
     * 
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
     * @param email of the user
     * @return true if the email exist in the DB otherwise return false
     */
    public boolean isEmailRegistered(String email) {
        log.debug("Looking if the email {} exist in DB", email);
        return userRepository.existsByEmail(email);
    }

    /**
     * 
     * @param email of the user to register in DB
     * @return the user registered
     */
    public UserModel registerUser(String email) {
        log.debug("Registering a new user: {}", email);
        UserModel user = new UserModel();
        user.setEmail(email);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setRestaurantName(null);

        return userRepository.save(user);
    }

    /**
     * 
     * @param response      httpServletResponse
     * @param longTimeToken
     */
    public void createAuthCookie(HttpServletResponse response, String longTimeToken) {
        int cookieExpiration = 21600; // 6h expiration

        Cookie tokenCookie = new Cookie("authToken", longTimeToken);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(cookieExpiration);
        tokenCookie.setHttpOnly(true);
        // cookie.setSecure(true); for https

        Cookie isConnectedCookie = new Cookie("isConnected", "1");
        isConnectedCookie.setPath("/");
        isConnectedCookie.setMaxAge(cookieExpiration);
        // cookie.setSecure(true); for https

        response.addCookie(tokenCookie);
        response.addCookie(isConnectedCookie);
    }

    /**
     * 
     * @param response httpServletResponse
     */
    public void deleteAuthCookie(HttpServletResponse response) {
        Cookie tokenCookie = new Cookie("authToken", "0");
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(0);

        Cookie isConnectedCookie = new Cookie("isConnected", "0");
        isConnectedCookie.setPath("/");
        isConnectedCookie.setMaxAge(0);

        response.addCookie(tokenCookie);
        response.addCookie(isConnectedCookie);
    }

    /**
     * 
     * @param email          of the user to put in the token
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
