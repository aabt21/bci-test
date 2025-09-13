package com.example.bcitest.infrastructure.controller;

import com.example.bcitest.application.port.UserPort;
import com.example.bcitest.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    private final UserPort userService;

    public UserRestController(UserPort userService) {
        this.userService = userService;
    }

    @PostMapping("/sing-up")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
