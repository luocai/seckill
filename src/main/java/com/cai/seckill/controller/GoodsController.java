package com.cai.seckill.controller;

import com.cai.seckill.pojo.Goods;
import com.cai.seckill.service.GoodsService;
import com.cai.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/listGoods")
    public String listGoods(Model model){

        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        System.out.println(goodsVos.size() + " ...");
        model.addAttribute("goodsVos",goodsVos);

        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String toDetail(Model model,@PathVariable("goodsId")long goodsId){
        GoodsVo goods = goodsService.getById(goodsId);
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int state = 0;
        long remainseconds = 0;

        if(now < start){
            state = 0; //秒杀还没开始
            remainseconds = start - now;
        }else if(now < end){
            state = 1; //正在秒杀
            remainseconds = 0;
        }else{
            state = 2; // 秒杀已结束
            remainseconds = -1;
        }

        System.out.println(remainseconds+"fuck");
        model.addAttribute("goods",goods);
        model.addAttribute("state",state); //remainseconds
        model.addAttribute("remainseconds",remainseconds);

        return "goods_detail";
    }
}
