package com.example.bcitest.infrastructure.controller;

import com.example.bcitest.application.port.UserPort;
import com.example.bcitest.application.usecase.GetUserLoginUseCase;
import com.example.bcitest.domain.model.User;
import com.example.bcitest.infrastructure.security.JwtService;
import com.example.bcitest.userinterface.exception.CredentialsNotFoundException;
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
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CredentialsNotFoundException("Invalid authorization header");
        }
        String token = authorizationHeader.substring(7);
        User user = getUserLoginUseCase.execute(token);
        return ResponseEntity.ok(user);
    }

}
