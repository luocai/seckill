package com.cai.seckill.rabbitmq;

import com.cai.seckill.pojo.Order;
import com.cai.seckill.pojo.User;
import com.cai.seckill.service.GoodsService;
import com.cai.seckill.service.OrderService;
import com.cai.seckill.service.SeckillService;
import com.cai.seckill.util.BeanUtil;
import com.cai.seckill.vo.GoodsVo;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqReciever {

//    @RabbitListener(queues = MqConfig.QUEUE)
//    public void recieve(String msg){
//        System.out.println("收到啦---"+msg);
//    }

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    /*
    当队列中有请求时，进行处理
    相当于把秒杀分成了两步，第一步在controller过滤无效请求，第二步则是在队列中进行真实的秒杀行为（即减库存，生成订单等
     */
    @RabbitListener(queues = MqConfig.seckill_QUEUE)
    public void recieve(String msg){
        System.out.println("rabbitmq ： 老子再处理啦 ");
        SeckillMsg message = BeanUtil.stringToBean(msg,SeckillMsg.class);

        User user = message.getUser();
        long goodsId = message.getGoodsId();

        GoodsVo goodsVo = goodsService.getById(goodsId);
        if(goodsVo.getStockCount() <= 0)
            return;
        //判断是否重复秒杀
        Order order = orderService.getOrderByGoodsId(user.getId(),goodsId);
        if(order != null)
            return;
        //减库存 下订单 写入秒杀订单
        seckillService.doSeckill(user,goodsVo);
    }
}
