package com.miaosha.uitl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.miaosha.entity.Admin;
import com.miaosha.entity.User;

public class TokenUtil {

    public static String getUserToken(User user){
        String token = JWT.create().withClaim("uid",user.getUid())
                .withClaim("phone", user.getPhone()).withClaim("type","user")
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    public static String getAdminToken(Admin admin){
        String token = JWT.create().withClaim("uid",admin.getAid())
                .withClaim("phone", admin.getWorkerID()).withClaim("type", "admin")
                .sign(Algorithm.HMAC256(admin.getPassword()));
        return token;
    }
}
