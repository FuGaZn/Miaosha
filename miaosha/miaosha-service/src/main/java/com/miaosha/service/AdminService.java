package com.miaosha.service;

import com.miaosha.entity.Admin;

public interface AdminService {
    boolean login(String workerID, String password);

    Admin findByWorkerID(String workerID);

    public boolean login(int aid, String password);

    Admin findByAid(int aid);
}
