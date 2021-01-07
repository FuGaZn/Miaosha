package com.miaosha.controller;

import com.google.common.util.concurrent.RateLimiter;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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

  //  @ResponseBody
   // @PostMapping("/coupon/order")
  //  @UserLoginToken
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
     * @param cid
     * @param uid
     * @return
     */
    @ResponseBody
    @PostMapping("/coupon/order/check")
    public Msg checkOrderInCache(int cid, int uid){
        Msg msg = new Msg(20000,"success");
        CouponOrder couponOrder = couponOrderService.checkCouponOrderInCache(cid,uid);
        if (couponOrder != null && couponOrder.getSuccess() == 0){
            return new Msg(50000,"fail");
        }
        if (couponOrder == null){
            couponOrder = couponOrderService.checkCouponOrderInDB(cid, uid);
            if (couponOrder == null)
                return new Msg(50000,"fail");
        }
        return msg;
    }

    @ResponseBody
    @PostMapping("/coupon/order/create")
    //  @UserLoginToken
    public Msg createCouponOrderMQ(@RequestBody CouponOrderVO order){
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)){
            Logger.getGlobal().info("限流，失败");
            Msg msg = new Msg(50000,"抢票失败");
            return msg;
        }

        // 判断用户是否已抢购过优惠券
        CouponOrder couponOrder = couponOrderService.checkCouponOrderInCache(order.getCid(), order.getUid());
        if (couponOrder != null){
            Logger.getGlobal().info("用户已抢购过优惠券");
            return new Msg(20000,"抢票成功");
        }

        int couponCount = couponService.getCouponCount(order.getCid());
        if (couponCount <= 0){
            return new Msg(50000,"优惠券被抢光啦！下次再来吧");
        }

        Logger.getGlobal().info("有库存，下一步");
        try {
            producer.sendMessage(order);
            return new Msg(20000,"正在排队中...");
        }catch (Exception e){
            e.printStackTrace();
        }

        return new Msg(50000,"抢票失败");
    }
}
