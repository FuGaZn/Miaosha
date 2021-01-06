package com.miaosha.uitl.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.miaosha.uitl.vo.CouponOrderVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(CouponOrderVO orderVO) throws JsonProcessingException {
        Gson gson = new Gson();
        rabbitTemplate.convertAndSend("topic.e", "r",gson.toJson(orderVO));
    }
}
