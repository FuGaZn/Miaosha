package com.miaosha.uitl.mq;

import com.google.gson.Gson;
import com.miaosha.entity.CouponOrder;
import com.miaosha.service.CouponService;
import com.miaosha.service.impl.CouponOrderServiceImpl;
import com.miaosha.service.impl.CouponServiceImpl;
import com.miaosha.uitl.vo.CouponOrderVO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqConsumer {

    @Autowired
    CouponOrderServiceImpl couponOrderService;

    @RabbitListener(queues = "topic.a")
    public void listen(String msg) {
        Gson gson = new Gson();
        CouponOrderVO orderVO = gson.fromJson(msg, CouponOrderVO.class);
        couponOrderService.tryCreateCouponOrder(orderVO.getCid(), orderVO.getUid());
    }
}
