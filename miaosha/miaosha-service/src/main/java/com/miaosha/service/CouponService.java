package com.miaosha.service;

import com.miaosha.entity.Coupon;
import com.miaosha.entity.CouponOrder;
import com.miaosha.entity.User;

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

    /**
     * 将购买记录持久化
     * @param coupon
     * @return
     */
    public boolean saveCouponRecord(Coupon coupon, int uid);

    /**
     * 通过消息队列执行持久化操作
     * @param coupon
     * @param uid
     * @return
     */
    public void saveCouponRecordByMQ(Coupon coupon, int uid);

    /**
     * 试图创建订单，如果创建成功，返回id
     * @param cid
     * @param uid
     * @return
     */
    public boolean tryCreateCouponRecord(int cid, int uid);

    public boolean checkCouponOrderInCache(int cid, int uid);

    public void delCouponCountCache(int cid);

    public void writeCouponOrderInCache(Coupon coupon, int uid);
}
