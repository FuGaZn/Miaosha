package com.miaosha.controller;

import com.auth0.jwt.JWT;
import com.miaosha.annotation.UserLoginToken;
import com.miaosha.entity.Coupon;
import com.miaosha.entity.CouponOrder;
import com.miaosha.service.impl.CouponOrderServiceImpl;
import com.miaosha.service.impl.CouponServiceImpl;
import com.miaosha.uitl.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 优惠券的添加、查看、杀出
 */
@Controller
public class CouponController {
    @Autowired
    CouponServiceImpl couponService;
    @Autowired
    CouponOrderServiceImpl couponOrderService;

    @GetMapping("/coupon/list")
    @ResponseBody
    public Msg getCouponList(){
        Msg msg = new Msg(20000,"");
        List<Coupon> coupons = couponService.getCouponList();
        msg.getData().put("coupons", coupons);
        return msg;
    }
    @GetMapping("/coupon/list/my")
    @ResponseBody
    @UserLoginToken
    public Msg getCouponListMy(@RequestHeader("X-token") String token){
        int uid = JWT.decode(token).getClaim("uid").asInt();
        Msg msg = new Msg(20000,"");
        List<CouponOrder> coupons =couponOrderService.getMyCouponOrders(uid);
        msg.getData().put("coupons", coupons);
        return msg;
    }
}
