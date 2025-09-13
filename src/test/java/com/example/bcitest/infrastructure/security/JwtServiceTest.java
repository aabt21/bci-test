package com.example.bcitest.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateToken_ShouldGenerateNonNullNonEmptyToken_WhenGeneratingToken() {
        String email = "test@example.com";

        String token = jwtService.generateToken(email);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_ShouldContainCorrectSubject_WhenGeneratingToken() {
        String email = "test@example.com";

        String token = jwtService.generateToken(email);
        String extractedEmail = jwtService.extractUsername(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    void extractUsername_ShouldFailToExtractUsername_WhenTamperedToken() {
        String email = "test@example.com";
        String token = jwtService.generateToken(email);
        String tamperedToken = token + "tamper";

        assertThrows(io.jsonwebtoken.security.SignatureException.class, () -> jwtService.extractUsername(tamperedToken),
                "Extracting username from a tampered token should throw SignatureException");
    }
}