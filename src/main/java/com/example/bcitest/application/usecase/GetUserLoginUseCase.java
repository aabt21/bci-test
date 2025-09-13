package com.example.bcitest.application.usecase;

import com.example.bcitest.domain.model.User;
import com.example.bcitest.infrastructure.database.UserRepositoryAdapter;
import com.example.bcitest.infrastructure.security.JwtService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GetUserLoginUseCase {

    private final UserRepositoryAdapter userRepository;
    private final JwtService jwtService;

    public GetUserLoginUseCase(UserRepositoryAdapter userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public User execute(String token) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setLastLogin(LocalDateTime.now());
        String newToken = jwtService.generateToken(email);
        user.setToken(newToken);
        return userRepository.save(user);
    }
}
