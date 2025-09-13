package com.example.bcitest.application.port;

import com.example.bcitest.domain.model.User;

public interface UserPort {
    User createUser(User user);
}
