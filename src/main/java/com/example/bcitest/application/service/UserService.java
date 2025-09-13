package com.example.bcitest.application.service;

import com.example.bcitest.application.port.UserPort;
import com.example.bcitest.domain.model.User;
import com.example.bcitest.domain.service.UserValidationService;
import com.example.bcitest.infrastructure.database.PhoneEntity;
import com.example.bcitest.infrastructure.database.UserRepositoryAdapter;
import com.example.bcitest.infrastructure.security.JwtService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserPort {

    private final UserRepositoryAdapter userRepository;
    private final JwtService jwtService;

    public UserService(UserRepositoryAdapter userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public User createUser(User user) {
        if (!UserValidationService.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!UserValidationService.isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password format");
        }
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        user.setActive(true);
        user.setToken(jwtService.generateToken(user.getEmail()));
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

}
