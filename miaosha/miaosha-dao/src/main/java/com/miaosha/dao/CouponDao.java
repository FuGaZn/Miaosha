package com.miaosha.dao;

import com.miaosha.entity.Coupon;
import com.miaosha.entity.CouponOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CouponDao extends JpaRepository<Coupon, Long> {
    Coupon findByCid(int cid);

   // List<Coupon> findAll();

    @Modifying
    @Transactional
    @Query("update Coupon set sale=sale+1 where cid=?1 and sale=?2")
    int updateOptimisticLock(int cid, int coupon_sale);
}
