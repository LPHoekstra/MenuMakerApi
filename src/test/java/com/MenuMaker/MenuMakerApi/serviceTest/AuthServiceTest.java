package com.MenuMaker.MenuMakerApi.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.MenuMaker.MenuMakerApi.model.UserModel;
import com.MenuMaker.MenuMakerApi.repository.UserRepository;
import com.MenuMaker.MenuMakerApi.service.AuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
public class AuthServiceTest {
    private String secret = "vE5v2pd7VWFA8DOhDBO+5Ecirg95kcs+14+iWUFP0S4=";
    private SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));

    private UserRepository userRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authService = new AuthService(secret, userRepository);
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
        // email validation
        assertEquals(userEmail, claims.getSubject(), "Expect token payload to be userEmail");

        // expiration validation
        long expiration = claims.getExpiration().getTime();
        long expectedExpiration = claims.getIssuedAt().getTime() + (10 * 60 * 1000);

        assertEquals(expectedExpiration, expiration, "Token must have a 10min expiration");
    }

    @Test
    void getEmailFromTokenWithSuccess() throws Exception {
        String userEmail = "test@gmail.com";

        // act
        String token = authService.shortTimeToken(userEmail);

        String email = authService.getEmailFromToken(token);

        assertEquals(userEmail, email);
    }

    @Test
    void emailExistInDB() throws Exception {
        String email = "test@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);
        // act
        boolean isEmailExist = authService.isEmailRegistered(email);

        assertTrue(isEmailExist);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void registerUserWithSuccess() throws Exception {
        String email = "test@gmail.com";

        UserModel user = new UserModel();
        user.setEmail(email);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setRestaurantName(null);

        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        // act
        UserModel userCreated = authService.registerUser(email);

        assertNotNull(userCreated, "userCreated should not be null");
        assertEquals(user.getEmail(), userCreated.getEmail(), "Emails should match");
        verify(userRepository, times(1)).save(any(UserModel.class));
    }
}
