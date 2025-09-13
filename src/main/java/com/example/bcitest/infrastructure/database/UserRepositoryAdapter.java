package com.example.bcitest.infrastructure.database;

import com.example.bcitest.domain.model.User;
import com.example.bcitest.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter {

    private final UserRepository repository;

    public UserRepositoryAdapter(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(UserMapper::toDomain);
    }

    public User save(User user) {
        var userEntity = UserMapper.toEntity(user);
        userEntity.getPhones().forEach(phone -> phone.setUser(userEntity));
        return UserMapper.toDomain(repository.save(userEntity));
    }
}
