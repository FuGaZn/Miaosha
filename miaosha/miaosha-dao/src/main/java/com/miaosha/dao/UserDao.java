package com.miaosha.dao;
import com.miaosha.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findByPhone(String phone);

    User findByUid(int id);
}
