package com.miaosha.service.impl;

import com.miaosha.dao.CouponDao;
import com.miaosha.dao.CouponOrderDao;
import com.miaosha.entity.Coupon;
import com.miaosha.entity.CouponOrder;
import com.miaosha.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private CouponOrderDao couponOrderDao;

    @Override
    public boolean saleCoupon(Coupon coupon) {
        int id = couponDao.updateOptimisticLock(coupon.getCid(), coupon.getSale());
        return id!=0;
    }

    @Override
    public Coupon checkCoupon(int cid) {
        Coupon coupon = couponDao.findByCid(cid);
        if (coupon.getSale() >= coupon.getTotal()) {
            return null;
        }
        return coupon;
    }

    @Override
    public boolean tryCreateCouponRecord(int cid, int uid) {
        Coupon coupon = checkCoupon(cid);
        if (coupon!=null){
            boolean b = saleCoupon(coupon);
            if (b == false){
                return false;
            }
            delCouponCountCache(cid);
            Logger.getGlobal().info("删除缓存");
            writeCouponOrderInCache(coupon, uid);
            Logger.getGlobal().info("保存订单至缓存");
            saveCouponRecord(coupon, uid);
            Logger.getGlobal().info("保存订单至数据库");
            return true;
        }
        return false;
    }

    @Override
    public boolean saveCouponRecord(Coupon coupon, int uid) {
        CouponOrder record = new CouponOrder();
        record.setDescription(coupon.getDescription());
        record.setUid(uid);
        record.setCid(coupon.getCid());
        int rid = couponOrderDao.save(record).getRid();
        return false;
    }

    @Override
    public void saveCouponRecordByMQ(Coupon coupon, int uid) {

    }

//    @Override
//    public void saveCouponRecordByMQ(Coupon coupon, int uid) {
//        CouponOrder record = new CouponOrder();
//        record.setDescription(coupon.getDescription());
//        record.setUid(uid);
//        record.setCid(coupon.getCid());
//        try {
//      //      producer.sendMessage(record);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public boolean checkCouponOrderInCache(int cid, int uid) {
        return false;
    }

    @Override
    public void delCouponCountCache(int cid) {

    }

    @Override
    public void writeCouponOrderInCache(Coupon coupon, int uid) {

    }
}
