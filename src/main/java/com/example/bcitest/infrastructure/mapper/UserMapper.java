package com.example.bcitest.infrastructure.mapper;

import com.example.bcitest.domain.model.User;
import com.example.bcitest.infrastructure.database.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public static User toDomain(UserEntity entity) {
        var phones = entity.getPhones() == null ? null : entity.getPhones().stream().map(PhoneMapper::toDomain).collect(Collectors.toList());
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .created(entity.getCreated())
                .lastLogin(entity.getLastLogin())
                .token(entity.getToken())
                .isActive(entity.isActive())
                .phones(phones)
                .build();
    }

    public static UserEntity toEntity(User domain) {
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setCreated(domain.getCreated());
        entity.setLastLogin(domain.getLastLogin());
        entity.setToken(domain.getToken());
        entity.setActive(domain.isActive());
        if (domain.getPhones() != null) {
            entity.setPhones(domain.getPhones().stream().map(PhoneMapper::toEntity).collect(Collectors.toList()));
        }
        return entity;
    }

}
