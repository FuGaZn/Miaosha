package com.miaosha.controller;

import com.auth0.jwt.JWT;
import com.miaosha.annotation.UserLoginToken;
import com.miaosha.dao.OperationLogDao;
import com.miaosha.dao.impl.OperationLogDaoImpl;
import com.miaosha.entity.Coupon;
import com.miaosha.entity.CouponOrder;
import com.miaosha.entity.OperationLog;
import com.miaosha.service.impl.AdminServiceImpl;
import com.miaosha.service.impl.CouponOrderServiceImpl;
import com.miaosha.service.impl.CouponServiceImpl;
import com.miaosha.service.impl.OperationLogServiceImpl;
import com.miaosha.uitl.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * 优惠券的添加、查看、杀出
 */
@Controller
public class CouponController {
    @Autowired
    CouponServiceImpl couponService;
    @Autowired
    CouponOrderServiceImpl couponOrderService;
    @Autowired
    AdminServiceImpl adminService;
    @Autowired
    OperationLogServiceImpl operationLogService;

    @GetMapping("/coupon/list/able")
    @ResponseBody
    public Msg getAbleCouponList() {
        Msg msg = new Msg(20000, "");
        List<Coupon> coupons = couponService.getAbleCouponList();
        msg.getData().put("coupons", coupons);
        return msg;
    }

    @GetMapping("/coupon/list/my")
    @ResponseBody
    public Msg getCouponListMy(@RequestHeader("X-token") String token) {
        int uid = JWT.decode(token).getClaim("uid").asInt();
        Msg msg = new Msg(20000, "");
        List<CouponOrder> coupons = couponOrderService.getMyCouponOrders(uid);
        msg.getData().put("coupons", coupons);
        return msg;
    }

    @GetMapping("/coupon/list/all")
    @ResponseBody
    public Msg getCouponList() {
        Msg msg = new Msg(20000, "");
        List<Coupon> coupons = couponService.getCouponList();
        msg.getData().put("coupons", coupons);
        return msg;
    }

    @PostMapping("/coupon/save1")
    @ResponseBody
    public Msg test(@RequestBody Coupon coupon,
                          @RequestHeader("X-token") String token, @RequestHeader("X-Ip") String ip) {
        Msg msg = new Msg(20000, "");
        int aid = JWT.decode(token).getClaim("uid").asInt();
            int cid = couponService.saveCoupon(coupon);
            coupon.setCid(cid);

            msg.getData().put("cid", cid);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            OperationLog log = new OperationLog();
            log.setIp_address(ip);
            log.setTime(format.format(new Date()));
            log.setWorkerID(adminService.findByAid(aid).getWorkerID());
            log.setDescription("Save coupon: "+coupon.toString());
            operationLogService.saveOperationLog(log);
        return msg;
    }

    @PostMapping("/coupon/save")
    @ResponseBody
    public Msg saveCoupon(@RequestParam("password") String password, @RequestBody Coupon coupon,
                          @RequestHeader("X-token") String token, @RequestHeader("X-Ip") String ip) {
        Logger.getGlobal().info("save "+password+" "+coupon.toString()+" "+ip);
        Msg msg = new Msg(20000, "");
        int aid = JWT.decode(token).getClaim("aid").asInt();
        boolean b = adminService.login(aid, password);
        if (b == true) {
            int cid = couponService.saveCoupon(coupon);
            coupon.setCid(cid);

            msg.getData().put("cid", cid);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            OperationLog log = new OperationLog();
            log.setIp_address(ip);
            log.setTime(format.format(Calendar.getInstance()));
            log.setWorkerID(adminService.findByAid(aid).getWorkerID());
            log.setDescription("Save coupon: "+coupon.toString());

            operationLogService.saveOperationLog(log);

        } else {
            msg = new Msg(50000, "请重新验证密码");
        }
        return msg;
    }

    @PostMapping("/coupon/publish")
    @ResponseBody
    public Msg publishCoupon(@RequestParam("password") String password, @RequestBody Coupon coupon,
                             @RequestHeader("X-token") String token, @RequestHeader("X-Ip") String ip) {
        Logger.getGlobal().info("publish "+password+" "+coupon.toString()+" "+ip);
        Msg msg = new Msg(20000, "");
        int aid = JWT.decode(token).getClaim("aid").asInt();
        boolean b = adminService.login(aid, password);
        if (b == true) {
            Coupon coupon1 = couponService.publishCoupon(coupon.getCid());
            msg.getData().put("coupon", coupon1);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            OperationLog log = new OperationLog();
            log.setIp_address(ip);
            log.setTime(format.format(Calendar.getInstance()));
            log.setWorkerID(adminService.findByAid(aid).getWorkerID());
            log.setDescription("Publish coupon: "+coupon1.toString());

            operationLogService.saveOperationLog(log);
        } else {
            msg = new Msg(50000, "请重新验证密码");
        }
        return msg;
    }


    @PostMapping("/coupon/delete")
    @ResponseBody
    public Msg deleteCoupon(@RequestParam("password") String password, @RequestBody Coupon coupon,
                             @RequestHeader("X-token") String token, @RequestHeader("X-Ip") String ip) {
        Msg msg = new Msg(20000, "");
        int aid = JWT.decode(token).getClaim("aid").asInt();
        boolean b = adminService.login(aid, password);
        if (b == true) {
            couponService.deleteCoupon(coupon.getCid());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            OperationLog log = new OperationLog();
            log.setIp_address(ip);
            log.setTime(format.format(Calendar.getInstance()));
            log.setWorkerID(adminService.findByAid(aid).getWorkerID());
            log.setDescription("Delete coupon: "+coupon.toString());

            operationLogService.saveOperationLog(log);
        } else {
            msg = new Msg(50000, "请重新验证密码");
        }
        return msg;
    }

}
