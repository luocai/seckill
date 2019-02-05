package com.cai.seckill.service;

import com.cai.seckill.pojo.Goods;
import com.cai.seckill.pojo.Order;
import com.cai.seckill.pojo.User;
import com.cai.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecikllService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Transactional
    public Order doSeckill(User user, GoodsVo goods){
        //减库存
        goodsService.reducestock(goods);

        //生成订单项进入数据库中
        return orderService.createOrder(user, goods);
    }

}
