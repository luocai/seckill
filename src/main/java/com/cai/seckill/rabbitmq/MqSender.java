package com.cai.seckill.rabbitmq;

import com.cai.seckill.util.BeanUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

//    public void send(String msg){
//
//        amqpTemplate.convertAndSend(MqConfig.QUEUE,msg);
//    }

    public void send(SeckillMsg msg){
        String message = BeanUtil.beanToString(msg);
        amqpTemplate.convertAndSend(MqConfig.seckill_QUEUE,message);
    }
}
