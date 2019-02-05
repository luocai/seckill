package com.cai.seckill.service;

import com.cai.seckill.dao.OrderDao;
import com.cai.seckill.pojo.Order;
import com.cai.seckill.pojo.User;
import com.cai.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    public Order createOrder(User user, GoodsVo goods){
        Order order = new Order();

        order.setCreateDate(new Date());
        order.setDeliveryAddrId(0L);
        order.setGoodsCount(1);
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsPrice(goods.getMiaoshaPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setUserId(user.getId());
        orderDao.createOrder(order);
        return order;
    }


    public Order getOrderByGoodsId(long userId,long goodsid) {
        return orderDao.getByGoodsId(userId,goodsid);
    }
}
