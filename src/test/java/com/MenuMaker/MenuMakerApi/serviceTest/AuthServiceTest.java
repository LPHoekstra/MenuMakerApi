package com.MenuMaker.MenuMakerApi.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.MenuMaker.MenuMakerApi.model.DAO.UserModel;
import com.MenuMaker.MenuMakerApi.repository.UserRepository;
import com.MenuMaker.MenuMakerApi.service.AuthService;

@SpringBootTest
public class AuthServiceTest {

    private UserRepository userRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authService = new AuthService(userRepository);
    }

    @Test
    void registerIfEmailNotRegistered_withSucces() {
        String email = "test@gmail.com";

        UserModel newUser = new UserModel.Builder().email(email).build();

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(UserModel.class))).thenReturn(newUser);

        // act
        UserModel userCreated = authService.registerIfEmailNotRegistered(email);

        assertEquals(newUser.getEmail(), userCreated.getEmail(), "Emails should match");
        verify(userRepository, times(1)).save(any(UserModel.class));
    }

    @Test
    void registerIfEmailNotRegistered_alreadyRegistered() {
        String email = "test@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        // act
        UserModel userCreated = authService.registerIfEmailNotRegistered(email);

        assertNull(userCreated, "Must return null when is already registered");
        verify(userRepository, times(1)).existsByEmail(any(String.class));
    }
}
