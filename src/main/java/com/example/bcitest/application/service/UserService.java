package com.example.bcitest.application.service;

import com.example.bcitest.application.port.UserPort;
import com.example.bcitest.domain.model.User;
import com.example.bcitest.domain.service.UserValidationService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserPort {

    @Override
    public User createUser(User user) {
        if (!UserValidationService.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!UserValidationService.isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password format");
        }
        return null;
    }

}
