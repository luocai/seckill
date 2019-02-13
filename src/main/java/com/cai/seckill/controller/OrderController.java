package com.cai.seckill.controller;

import com.cai.seckill.common.Constants;
import com.cai.seckill.common.ResponseResult;
import com.cai.seckill.pojo.Order;
import com.cai.seckill.pojo.User;
import com.cai.seckill.redis.RedisService;
import com.cai.seckill.service.GoodsService;
import com.cai.seckill.service.OrderService;
import com.cai.seckill.vo.GoodsVo;
import com.cai.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.transform.Result;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public ResponseResult orderdetail(@RequestParam("orderId") long orderId,User user){

        if(user == null){
            return new ResponseResult(Constants.NEED_LOGIN,"未登录");
        }
        Order order = orderService.getOrderById(orderId);
        System.out.println(order);
        if(order == null){
            return new ResponseResult(Constants.ORDER_NOT_EXIST,"订单不存在");
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getById(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setGoods(goods);
        vo.setOrder(order);
        System.out.println("OUT");
        return new ResponseResult(Constants.SUCCESS,vo);
    }
}
