package com.miaosha.service.impl;

import com.google.gson.Gson;
import com.miaosha.dao.CouponOrderDao;
import com.miaosha.entity.Coupon;
import com.miaosha.entity.CouponOrder;
import com.miaosha.service.CouponOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class CouponOrderServiceImpl implements CouponOrderService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private CouponOrderDao couponOrderDao;
    @Autowired
    private CouponServiceImpl couponService;

    @Override
    public boolean tryCreateCouponOrder(int cid, int uid) {
        Logger.getGlobal().info("试图创建订单");
        Coupon coupon = couponService.checkCoupon(cid);
        if (coupon != null) {
            boolean b = couponService.saleCoupon(coupon);
            if (b != false) {
                delCouponCountCache(cid);
                Logger.getGlobal().info("删除缓存cid");
                writeCouponOrderInCache(coupon, uid);
                Logger.getGlobal().info("保存订单至缓存");
                saveCouponOrder(coupon, uid);
                Logger.getGlobal().info("保存订单至数据库");
                return true;
            }
        }
      //  writeFailCouponOrderInCache(cid, uid);
        return false;
    }

    @Override
    public boolean saveCouponOrder(Coupon coupon, int uid) {
        CouponOrder record = new CouponOrder();
        record.setDescription(coupon.getDescription());
        record.setUid(uid);
        record.setCid(coupon.getCid());
        int rid = couponOrderDao.save(record).getRid();
        return false;
    }

    @Override
    public CouponOrder checkCouponOrderInCache(int cid, int uid) {
        String key = cid + "_" + uid+"_cr";
        String json = redisTemplate.opsForValue().get(key);
        if (json == null || json.length() == 0)
            return null;
        Gson gson = new Gson();
        return gson.fromJson(json, CouponOrder.class);
    }

    @Override
    public void resetCache() {
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }

    @Override
    public CouponOrder checkCouponOrderInDB(int cid, int uid) {
        return couponOrderDao.findByCidAndUid(cid, uid);
    }

    @Override
    public void delCouponCountCache(int cid) {
        int value = Integer.valueOf(redisTemplate.opsForValue().get("cid_"+cid));
        redisTemplate.opsForValue().set(""+cid,""+(value-1));
    }

    @Override
    public void writeCouponOrderInCache(Coupon coupon, int uid) {
        Logger.getGlobal().info("保存订单至缓存-2");
        CouponOrder couponOrder = new CouponOrder();
        couponOrder.setCid(coupon.getCid());
        couponOrder.setUid(uid);
        couponOrder.setSuccess(1);
        String key = coupon.getCid() + "_" + uid+"_cr";
        Gson gson = new Gson();
        redisTemplate.opsForValue().set(key, gson.toJson(couponOrder));
    }

    @Override
    public void writeFailCouponOrderInCache(int cid, int uid) {
        Logger.getGlobal().info("保存失败订单至缓存");
        CouponOrder couponOrder = new CouponOrder();
        couponOrder.setCid(cid);
        couponOrder.setUid(uid);
        couponOrder.setSuccess(0);
        String key = cid + "_" + uid+"_cr";
        Gson gson = new Gson();
        redisTemplate.opsForValue().set(key, gson.toJson(couponOrder));
    }

    @Override
    public List<CouponOrder> getMyCouponOrders(int uid) {
        return couponOrderDao.findByUid(uid);
    }
}
