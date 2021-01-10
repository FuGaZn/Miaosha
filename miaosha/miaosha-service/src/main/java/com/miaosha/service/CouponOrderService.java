package com.miaosha.service;

import com.miaosha.entity.Coupon;
import com.miaosha.entity.CouponOrder;

import java.util.List;

public interface CouponOrderService {
    /**
     * 将购买记录持久化
     * @param coupon
     * @return
     */
    public boolean saveCouponOrder(Coupon coupon, int uid);

    /**
     * 试图创建订单，如果创建成功，返回id
     * @param cid
     * @param uid
     * @return
     */
    public boolean tryCreateCouponOrder(int cid, int uid);

    public CouponOrder checkCouponOrderInCache(int cid, int uid);

    public CouponOrder checkCouponOrderInDB(int cid, int uid);

    public void delCouponCountCache(int cid);

    public void writeCouponOrderInCache(Coupon coupon, int uid);

    public void writeFailCouponOrderInCache(int cid, int uid);

    public List<CouponOrder> getMyCouponOrders(int uid);

    public void resetCache();
}
