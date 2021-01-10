package com.miaosha.dao;

import com.miaosha.entity.Coupon;
import com.miaosha.entity.CouponOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponOrderDao extends JpaRepository<CouponOrder, Long> {

    List<CouponOrder> findByUid(int uid);

    List<CouponOrder> findByCid(int cid);

    CouponOrder findByCidAndUid(int cid, int uid);

}
