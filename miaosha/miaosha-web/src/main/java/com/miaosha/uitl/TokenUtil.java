package com.miaosha.uitl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.miaosha.entity.User;

public class TokenUtil {

    public static String getUserToken(User user){
        String token = JWT.create().withClaim("uid",user.getUid())
                .withClaim("phone", user.getPhone())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
