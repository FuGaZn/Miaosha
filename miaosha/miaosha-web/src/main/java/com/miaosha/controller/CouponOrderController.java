package com.miaosha.controller;

import com.auth0.jwt.JWT;
import com.google.common.util.concurrent.RateLimiter;
import com.miaosha.annotation.UserLoginToken;
import com.miaosha.entity.Coupon;
import com.miaosha.entity.CouponOrder;
import com.miaosha.service.impl.CouponOrderServiceImpl;
import com.miaosha.service.impl.CouponServiceImpl;
import com.miaosha.uitl.Msg;
import com.miaosha.uitl.mq.MqProducer;
import com.miaosha.uitl.vo.CouponOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Controller
public class CouponOrderController {
    @Autowired
    CouponServiceImpl couponService;
    @Autowired
    CouponOrderServiceImpl couponOrderService;
    @Autowired
    MqProducer producer;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    RateLimiter rateLimiter = RateLimiter.create(20);

//    @ResponseBody
//    @PostMapping("/coupon/order")
//    @UserLoginToken
//    public Msg createCouponOrder(@RequestBody CouponOrderVO order){
//        Msg msg = null;
//        Logger.getGlobal().info("coupon order");
//
//        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)){
//            Logger.getGlobal().info("限流，失败");
//            msg = new Msg(50000,"抢票失败");
//            return msg;
//        }
//        try {
//            couponService.tryCreateCouponRecord(order.getCid(), order.getUid());
//            Logger.getGlobal().info("抢票成功");
//            msg = new Msg(20000,"抢票成功");
//            return msg;
//        }catch (Exception e){
//            Logger.getGlobal().info("抢票失败");
//            msg = new Msg(50000,"抢票失败");
//            return msg;
//        }
//    }

    /**
     * 前端轮询，判断订单是否处理成功
     * @param order
     * @return
     */
    @ResponseBody
    @PostMapping("/coupon/order/check")
    @UserLoginToken
    public Msg checkOrderInCache(@RequestBody CouponOrderVO order,@RequestHeader("X-token") String token){
        int cid = order.getCid();
        int uid = JWT.decode(token).getClaim("uid").asInt();
        Logger.getGlobal().info("check order "+cid+" "+uid);
        Msg msg = new Msg(20000,"success");
        CouponOrder couponOrder = couponOrderService.checkCouponOrderInCache(cid,uid);
        if (couponOrder != null && couponOrder.getSuccess() == 0){
            // 抢券失败
            return new Msg(50001,"fail");
        }
        if (couponOrder == null){
            Logger.getGlobal().info("缓存中没有order");
            couponOrder = couponOrderService.checkCouponOrderInDB(cid, uid);
            if (couponOrder == null)
                // 抢券结果还没出来
                return new Msg(50002,"fail");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/coupon/order/create")
    @UserLoginToken
    public Msg createCouponOrderMQ(@RequestBody CouponOrderVO order, @RequestHeader("X-token") String token){
        int uid = JWT.decode(token).getClaim("uid").asInt();
        int cid = order.getCid();
        Logger.getGlobal().info("cid "+cid+" "+"uid "+uid);
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)){
            Logger.getGlobal().info("限流，失败");
            Msg msg = new Msg(50001,"抢票失败");
            return msg;
        }

        // 判断用户是否已抢购过优惠券
        CouponOrder couponOrder = couponOrderService.checkCouponOrderInCache(cid, uid);
        if (couponOrder != null && couponOrder.getSuccess() == 1){
            Logger.getGlobal().info("用户已抢购过优惠券");
            return new Msg(20001,"您已经抢到了该优惠券");
        }

        int couponCount = couponService.getCouponCount(cid);
        if (couponCount <= 0){
            return new Msg(50002,"优惠券被抢光啦！下次再来吧");
        }

        Logger.getGlobal().info("有库存，下一步");
//        Coupon coupon = couponService.checkCoupon(order.getCid());
//        boolean b = couponService.saleCoupon(coupon);
        try {
            CouponOrderVO orderVO = new CouponOrderVO(cid, uid);
            producer.sendMessage(orderVO);
            return new Msg(20002,"正在排队中...");
        }catch (Exception e){
            e.printStackTrace();
        }

        return new Msg(50000,"抢票失败");
    }
}
