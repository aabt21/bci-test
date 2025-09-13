package com.example.bcitest.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy hh:mm:ss a")
    private LocalDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy hh:mm:ss a")
    private LocalDateTime lastLogin;
    private String token;
    private boolean isActive;
}
