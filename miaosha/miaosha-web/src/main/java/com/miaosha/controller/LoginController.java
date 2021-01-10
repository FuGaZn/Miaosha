package com.miaosha.controller;

import com.auth0.jwt.JWT;
import com.miaosha.annotation.UserLoginToken;
import com.miaosha.dao.UserDao;
import com.miaosha.entity.User;
import com.miaosha.service.UserService;
import com.miaosha.service.impl.CouponOrderServiceImpl;
import com.miaosha.service.impl.UserServiceImpl;
import com.miaosha.uitl.Msg;
import com.miaosha.uitl.TokenUtil;
import com.miaosha.util.MyMD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
public class LoginController {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    CouponOrderServiceImpl couponOrderService;
    @Autowired
    UserDao userDao;

    @PostMapping("/login")
    @ResponseBody
    public Msg login(@RequestBody User user){
        boolean b = userService.login(user);
        Msg msg = new Msg(20000,"success");
        if (b == false) {
            msg = new Msg(50000, "fail");
            return msg;
        }
        User user1 = userService.findUserByPhone(user.getPhone());
        String token = TokenUtil.getUserToken(user1);

        couponOrderService.resetCache();

        msg.getData().put("token", token);
        return msg;
    }

    @PostMapping("/register")
    @ResponseBody
    public Msg register(@RequestBody User user){
        Logger.getGlobal().info(user.toString());
        boolean b = userService.register(user);
        Msg msg = new Msg(20000,"success");
        if (b == false) {
            msg = new Msg(50000, "手机号已存在");
            return msg;
        }
        User user1 = userService.findUserByPhone(user.getPhone());
        String token = TokenUtil.getUserToken(user1);
        msg.getData().put("token", token);
        return msg;
    }

    @PostMapping("/user/modify/pwd")
    @ResponseBody
    public Msg modifyPassword(@RequestParam String oldPassword,@RequestParam String newPassword, @RequestHeader("X-token") String token){
        Msg msg = new Msg(20000,"success");
        if (token == null|| token.equals("")){
            msg.setMessage("登陆信息失效，请重新登陆");
            msg.setCode(50016);
            return msg;
        }
        int uid = JWT.decode(token).getClaim("uid").asInt();
        User user = userService.findUserByID(uid);
        if (user.getPassword().equals(MyMD5.encrypt(oldPassword+user.getSalt()))){
            user.setPassword(MyMD5.encrypt(newPassword+user.getSalt()));
            userService.updateUser(user);
        }else{
            msg.setMessage("原密码错误");
            msg.setCode(50016);
        }
        return msg;
    }

    @GetMapping("/user/info")
    @ResponseBody
    @UserLoginToken
    public Msg getInfo(@RequestHeader("X-token") String token){
        int uid = JWT.decode(token).getClaim("uid").asInt();
        Msg msg = new Msg(20000,"");
        User user = userService.findUserByID(uid);
        user.setPassword("");
        user.setUid(0);
        user.setSalt("");
        msg.getData().put("userInfo", user);
        return msg;
    }
}
