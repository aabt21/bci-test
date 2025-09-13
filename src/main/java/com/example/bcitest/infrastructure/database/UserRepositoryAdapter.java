package com.example.bcitest.infrastructure.database;

import com.example.bcitest.domain.model.User;
import com.example.bcitest.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserRepositoryAdapter(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> User.builder()
                        .id(entity.getId())
                        .email(entity.getEmail())
                        .password(entity.getPassword())
                        .created(entity.getCreated())
                        .lastLogin(entity.getLastLogin())
                        .token(entity.getToken())
                        .isActive(entity.isActive())
                        .build());
    }

    public User save(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setCreated(user.getCreated());
        entity.setLastLogin(user.getLastLogin());
        entity.setToken(user.getToken());
        entity.setActive(user.isActive());
        return mapper.toDomain(repository.save(entity));
    }
}
