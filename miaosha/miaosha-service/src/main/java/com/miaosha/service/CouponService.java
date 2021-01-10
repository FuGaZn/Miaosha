package com.miaosha.service;

import com.miaosha.entity.Coupon;
import com.miaosha.entity.CouponOrder;
import com.miaosha.entity.User;

import java.util.List;

public interface CouponService {


    /**
     * 卖出一张优惠券
     * @param coupon
     * @return 返回订单记录的id
     */
    public boolean saleCoupon(Coupon coupon);
    /**
     * 判断优惠券是否还有库存，如果有，返回优惠券对象
     * @param cid
     * @return
     */
    public Coupon checkCoupon(int cid);

    public int getCouponCount(int cid);

    public List<Coupon> getCouponList();

}
