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
                .map(mapper::toDomain);
    }

    public User save(User user) {
        return mapper.toDomain(repository.save(mapper.toEntity(user)));
    }
}
