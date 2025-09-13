package com.example.bcitest.application.service;

import com.example.bcitest.application.port.UserPort;
import com.example.bcitest.domain.model.User;
import com.example.bcitest.domain.service.UserValidationService;
import com.example.bcitest.infrastructure.database.UserRepositoryAdapter;
import com.example.bcitest.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserPort {

    private final UserRepositoryAdapter userRepository;

    public UserService(UserRepositoryAdapter userRepository) {
        this.userRepository = userRepository;
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
        return userRepository.save(user);
    }

}
