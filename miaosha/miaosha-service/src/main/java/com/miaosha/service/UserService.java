package com.miaosha.service;

import com.miaosha.entity.User;

public interface UserService {
    User findUserByPhone(String ph);

    User findUserByID(int id);

    void addUser(User user);

    void updateUser(User user);

    boolean register(User user);

    boolean login(User user);
}
