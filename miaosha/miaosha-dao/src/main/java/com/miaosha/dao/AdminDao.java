package com.miaosha.dao;

import com.miaosha.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDao extends JpaRepository<Admin, Long> {
    Admin findByAid(int aid);

    Admin findByWorkerID(String workerID);
}
