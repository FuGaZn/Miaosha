package com.miaosha.service.impl;

import com.miaosha.dao.UserDao;
import com.miaosha.entity.User;
import com.miaosha.service.UserService;
import com.miaosha.util.MyMD5;
import com.miaosha.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User findUserByPhone(String ph) {
        return userDao.findByPhone(ph);
    }

    @Override
    public User findUserByID(int id) {
        return userDao.findByUid(id);
    }

    @Override
    public void updateUser(User user) {
        userDao.save(user);
    }

    @Override
    public String findSaltByPhone(String ph) {
        return null;
    }

    @Override
    public boolean login(User user) {
        User user1 = findUserByPhone(user.getPhone());
        if (user1 == null)
            return false;
        if (user1.getPassword().equals(MyMD5.encrypt(user.getPassword() + user1.getSalt()))) {
            Logger.getGlobal().info("login success");
            return true;
        }
        return false;
    }

    @Override
    public void addUser(User user) {
        userDao.save(user);
    }

    @Override
    public boolean register(User user) {
        User user1 = findUserByPhone(user.getPhone());
        if (user1 != null)
            return false;
        String salt = StringUtil.getRandomString(6);
        user.setSalt(salt);
        user.setPassword(MyMD5.encrypt(user.getPassword() + salt));
        addUser(user);
        return true;
    }
}
