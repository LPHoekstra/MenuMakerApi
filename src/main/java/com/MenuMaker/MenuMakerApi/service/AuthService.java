package com.MenuMaker.MenuMakerApi.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.MenuMaker.MenuMakerApi.model.UserModel;
import com.MenuMaker.MenuMakerApi.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
