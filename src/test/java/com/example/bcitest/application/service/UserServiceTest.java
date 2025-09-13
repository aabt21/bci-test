package com.example.bcitest.application.service;

import com.example.bcitest.domain.model.User;
import com.example.bcitest.infrastructure.database.UserRepositoryAdapter;
import com.example.bcitest.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    private final UserRepositoryAdapter userRepository = Mockito.mock(UserRepositoryAdapter.class);
    private final JwtService jwtService = Mockito.mock(JwtService.class);
    private final UserService userService = new UserService(userRepository, jwtService);

    @Test
    void createUser_ShouldSuccessfullyCreatesUser_WhenValidDataIsPassed() {
        User user = User.builder()
                .email("test@example.com")
                .password("Strong12")
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(jwtService.generateToken("test@example.com")).thenReturn("sample-jwt-token");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("sample-jwt-token", createdUser.getToken());
        assertTrue(createdUser.isActive());
        assertNotNull(createdUser.getCreated());
        assertNotNull(createdUser.getLastLogin());
        verify(userRepository).save(createdUser);
    }

    @Test
    void createUser_ShouldThrowsException_WhenEmailIsInvalid() {
        User user = User.builder()
                .email("invalid-email")
                .password("Strong12")
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user)
        );

        assertEquals("Invalid email format", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowsException_WhenPasswordIsInvalid() {
        User user = User.builder()
                .email("test@example.com")
                .password("short")
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user)
        );

        assertEquals("Invalid password format", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {
        User existingUser = User.builder()
                .email("test@example.com")
                .password("Strong123")
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));

        User user = User.builder()
                .email("test@example.com")
                .password("Another123")
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user)
        );

        assertEquals("User already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}