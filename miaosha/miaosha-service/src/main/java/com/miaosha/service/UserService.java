package com.miaosha.service;

import com.miaosha.entity.User;
import org.springframework.stereotype.Service;

public interface UserService {
    User findUserByPhone(String ph);

    String findSaltByPhone(String ph);

    void addUser(User user);

    boolean register(User user);

    boolean login(User user);
}
