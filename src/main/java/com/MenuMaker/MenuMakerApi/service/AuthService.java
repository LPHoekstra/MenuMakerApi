package com.MenuMaker.MenuMakerApi.service;

import org.springframework.stereotype.Service;

import com.MenuMaker.MenuMakerApi.model.DAO.UserModel;
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
     * Check if the email is registered in the DB, if not the user is registered.
     * @param email from the user.
     * @return the registered user if registered, otherwise return null.
     */
    public UserModel registerIfEmailNotRegistered(String email) {
        if (!isEmailRegistered(email)) {
            return registerUser(email);
        }

        return null;
    }

    /**
     * Check if the email is registered as a user in the DB
     * @param email of the user
     * @return true if the email exist in the DB otherwise return false
     */
    private boolean isEmailRegistered(String email) {
        log.debug("Looking if the email {} exist in DB", email);
        return userRepository.existsByEmail(email);
    }

    /**
     * Register a new user in the DB, must check if a user with this email is already in DB.
     * @param email from user to register in DB.
     * @return the user registered.
     */
    private UserModel registerUser(String email) {
        log.debug("Registering a new user: {}", email);
        UserModel user = new UserModel.Builder().email(email).build();

        return userRepository.save(user);
    }

    /**
     * Create a authToken and isConnected cookie. Then add it to the {@link HttpServletResponse}.
     * @param servletResponse
     * @param longTimeToken
     */
    public void createAuthCookie(HttpServletResponse servletResponse, String longTimeToken) {
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

        servletResponse.addCookie(tokenCookie);
        servletResponse.addCookie(isConnectedCookie);
    }

    /**
     * Delete the cookie authToken and isConnected by setting there max age to 0.
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
