package com.miaosha.controller;

import com.miaosha.entity.User;
import com.miaosha.service.UserService;
import com.miaosha.service.impl.UserServiceImpl;
import com.miaosha.uitl.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
public class LoginController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping("/login")
    @ResponseBody
    public Msg login(@RequestBody User user){
        Logger.getGlobal().info("controller");
        Logger.getGlobal().info(user.getPassword());
        boolean b = userService.login(user);
        Msg msg = new Msg(20000,"success");
        if (b == false)
            msg = new Msg(50000,"fail");
        return msg;
    }

    @PostMapping("/register")
    public Msg register(User user){
        boolean b = userService.register(user);
        Msg msg = new Msg(20000,"success");
        if (b == false)
            msg = new Msg(50000,"手机号已存在");
        return msg;
    }
}
