package com.example.bcitest.infrastructure.controller;

import com.example.bcitest.application.port.UserPort;
import com.example.bcitest.application.usecase.GetUserLoginUseCase;
import com.example.bcitest.domain.model.User;
import com.example.bcitest.infrastructure.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRestController {
    private final UserPort userService;
    private final GetUserLoginUseCase getUserLoginUseCase;

    public UserRestController(UserPort userService, JwtService jwtService, GetUserLoginUseCase getUserLoginUseCase) {
        this.userService = userService;
        this.getUserLoginUseCase = getUserLoginUseCase;
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

    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = authorizationHeader.substring(7);
        User user = getUserLoginUseCase.execute(token);
        return ResponseEntity.ok(user);
    }

}
