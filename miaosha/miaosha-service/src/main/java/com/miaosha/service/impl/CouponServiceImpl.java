package com.miaosha.service.impl;

import com.miaosha.dao.CouponDao;
import com.miaosha.dao.CouponOrderDao;
import com.miaosha.entity.Coupon;
import com.miaosha.entity.CouponOrder;
import com.miaosha.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private CouponOrderDao couponOrderDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean saleCoupon(Coupon coupon) {
        int id = couponDao.updateOptimisticLock(coupon.getCid(), coupon.getSale());
        return id!=0;
    }

    /**
     * @param cid
     * @return
     */
    @Override
    public Coupon checkCoupon(int cid) {
        Coupon coupon = couponDao.findByCid(cid);
        if (coupon.getSale()<coupon.getTotal())
            return coupon;
        return null;
    }

    @Override
    public int getCouponCount(int cid) {
        int count = getCouponCountByCache(cid);
        if (count <= 0){
            count = getCouponCountByDB(cid);
            redisTemplate.opsForValue().set("cid_"+cid, ""+count);
        }
        return count;
    }


    public int getCouponCountByCache(int cid){
        String count = redisTemplate.opsForValue().get("cid_"+cid);
        if (count == null|| count.length() == 0)
            return 0;
        return Integer.valueOf(count);
    }


    public int getCouponCountByDB(int cid) {
        Coupon coupon = couponDao.findByCid(cid);
        return coupon.getTotal() - coupon.getSale();
    }

    @Override
    public List<Coupon> getCouponList() {
        return couponDao.findAll();
    }

    @Override
    public List<Coupon> getAbleCouponList() {
        return couponDao.findAllByAble(1);
    }

    @Override
    public int saveCoupon(Coupon coupon) {
        return couponDao.save(coupon).getCid();
    }

    @Override
    public Coupon publishCoupon(int cid) {
        Coupon coupon = couponDao.findByCid(cid);
        coupon.setAble(1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        coupon.setPublishTime(format.format(Calendar.getInstance()));
        return couponDao.save(coupon);
    }

    @Override
    public void deleteCoupon(int cid) {
        Coupon coupon = couponDao.findByCid(cid);
        coupon.setAble(0);
        couponDao.save(coupon);
    }
}
