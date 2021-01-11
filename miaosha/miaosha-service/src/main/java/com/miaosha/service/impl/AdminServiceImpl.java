package com.miaosha.service.impl;

import com.miaosha.dao.AdminDao;
import com.miaosha.entity.Admin;
import com.miaosha.entity.User;
import com.miaosha.service.AdminService;
import com.miaosha.util.MyMD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminDao adminDao;
    @Override
    public boolean login(String workerID, String password) {
        Admin admin1 = adminDao.findByWorkerID(workerID);
        if (admin1 == null)
            return false;
        if (admin1.getPassword().equals(MyMD5.encrypt(password + admin1.getSalt()))) {
      //      Logger.getGlobal().info("login success");
            return true;
        }
        return false;
    }

    @Override
    public boolean login(int aid, String password) {
        Admin admin1 = adminDao.findByAid(aid);
        if (admin1 == null)
            return false;
        if (admin1.getPassword().equals(MyMD5.encrypt(password + admin1.getSalt()))) {
            //      Logger.getGlobal().info("login success");
            return true;
        }
        return false;
    }

    @Override
    public Admin findByWorkerID(String workerID) {
        return adminDao.findByWorkerID(workerID);
    }


    @Override
    public Admin findByAid(int aid) {
        return adminDao.findByAid(aid);
    }
}
