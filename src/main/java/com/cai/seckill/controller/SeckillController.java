package com.cai.seckill.controller;

import com.cai.seckill.common.CodeMsg;
import com.cai.seckill.pojo.Order;
import com.cai.seckill.pojo.User;
import com.cai.seckill.service.GoodsService;
import com.cai.seckill.service.OrderService;
import com.cai.seckill.service.SecikllService;
import com.cai.seckill.vo.GoodsVo;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SecikllService secikllService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, User user, @RequestParam("goodsId") long goodsId){


        //未登陆则先登陆
//        if(user == null){
//            return "login";
//      .  }
        System.out.println(111);
        GoodsVo goods = goodsService.getById(goodsId);
        if(goods == null){

        }
        System.out.println(222);
        //判断是否秒杀结束
        if(goods.getStockCount() <= 0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER);
            return "miaosha_fail";
        }
        System.out.println(.333);
        //判断是否重复秒杀
        Order order = orderService.getOrderByGoodsId(user.getId(),goodsId);
        if(order != null){
            model.addAttribute("errmg", CodeMsg.REPEATE_MIAOSHA);
            return "miaosha_fail";
        }
        System.out.println(444);
        //进行秒杀（减库存，写入订单）
        order = secikllService.doSeckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        System.out.println(555);
        return "order_detail";
    }
}
