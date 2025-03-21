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
     * @param userEmail take the email to put in the token
     * @return the token with an expiration of 10min
     */
    public String shortTimeToken(String userEmail) {
        log.debug("Creating a short time token for email: {}", userEmail);
        final long JWT_EXPIRATION = 10 * 60 * 1000L;

        return Jwts.builder()
                .subject(userEmail)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + JWT_EXPIRATION))
                .signWith(secretKey)
                .compact();
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
}
