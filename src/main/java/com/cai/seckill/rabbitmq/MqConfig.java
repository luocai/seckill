package com.cai.seckill.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    public static final String seckill_QUEUE = "miaosha.queue";
    public static final String QUEUE = "queue";

    @Bean
    public Queue queue() {
        return new Queue(seckill_QUEUE, true);
    }
}
