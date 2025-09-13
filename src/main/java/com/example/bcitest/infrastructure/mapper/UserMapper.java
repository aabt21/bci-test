package com.example.bcitest.infrastructure.mapper;

import com.example.bcitest.domain.model.User;
import com.example.bcitest.infrastructure.database.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .created(entity.getCreated())
                .lastLogin(entity.getLastLogin())
                .token(entity.getToken())
                .isActive(entity.isActive())
                .build();
    }

    public UserEntity toEntity(User domain) {
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setCreated(domain.getCreated());
        entity.setLastLogin(domain.getLastLogin());
        entity.setToken(domain.getToken());
        entity.setActive(domain.isActive());
        return entity;
    }

}
