package com.example.bcitest.application.usecase;

import com.example.bcitest.domain.model.User;
import com.example.bcitest.infrastructure.database.UserRepositoryAdapter;
import com.example.bcitest.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GetUserLoginUseCaseTest {

    private UserRepositoryAdapter mockRepository;
    private JwtService mockJwtService;
    private GetUserLoginUseCase getUserLoginUseCase;

    @BeforeEach
    void setUp() {
        mockRepository = mock(UserRepositoryAdapter.class);
        mockJwtService = mock(JwtService.class);
        getUserLoginUseCase = new GetUserLoginUseCase(mockRepository, mockJwtService);
    }

    @Test
    void execute_ShouldReturnUpdatedUser_WhenTokenIsValid() {
        String token = "valid.token.value";
        String email = "test@example.com";
        String newToken = "new.generated.token";

        User existingUser = User.builder()
                .email(email)
                .password("Password123")
                .created(LocalDateTime.now().minusDays(1))
                .lastLogin(LocalDateTime.now().minusDays(1))
                .build();

        User updatedUser = User.builder()
                .email(email)
                .password("Password123")
                .created(existingUser.getCreated())
                .lastLogin(LocalDateTime.now())
                .token(newToken)
                .build();

        when(mockJwtService.extractUsername(token)).thenReturn(email);
        when(mockRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(mockJwtService.generateToken(email)).thenReturn(newToken);
        when(mockRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);

        User result = getUserLoginUseCase.execute(token);

        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getToken(), result.getToken());
        assertEquals(updatedUser.getLastLogin().toLocalDate(), result.getLastLogin().toLocalDate());
        verify(mockRepository).findByEmail(email);
        verify(mockJwtService).extractUsername(token);
        verify(mockJwtService).generateToken(email);
        verify(mockRepository).save(Mockito.any(User.class));
    }

    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {
        String token = "valid.token.value";
        String email = "notfound@example.com";

        when(mockJwtService.extractUsername(token)).thenReturn(email);
        when(mockRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> getUserLoginUseCase.execute(token));
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(mockRepository).findByEmail(email);
        verify(mockJwtService).extractUsername(token);
        verify(mockRepository, never()).save(Mockito.any(User.class));
    }
}