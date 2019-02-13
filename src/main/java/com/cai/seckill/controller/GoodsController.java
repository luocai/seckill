package com.cai.seckill.controller;

import com.cai.seckill.common.Constants;
import com.cai.seckill.common.ResponseResult;
import com.cai.seckill.pojo.User;
import com.cai.seckill.redis.keys.GoodsKey;
import com.cai.seckill.redis.RedisService;
import com.cai.seckill.service.GoodsService;
import com.cai.seckill.service.UserService;
import com.cai.seckill.vo.GoodsDetailVo;
import com.cai.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {


    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/listGoods",produces = "text/html")
    @ResponseBody
    public String listGoods(HttpServletRequest request, HttpServletResponse response,Model model, User user){

        model.addAttribute("user",user);

        //先从缓存取
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if(!StringUtils.isEmpty(html)){
            System.out.println("缓存");
            return html;
        }
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsVos",goodsVos);

        //手动渲染
        //注意 springboot1中是 SpringWebContext ctx = new SpringWebContext(request,response,
        //    			request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
        WebContext ctx = new WebContext(request,response,request.getServletContext(),response.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        //放入缓存，下次可以直接在缓存取
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String toDetail2(HttpServletRequest request,HttpServletResponse response, User user,Model model,
                           @PathVariable("goodsId")long goodsId){
        model.addAttribute("user",user);
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        if(!StringUtils.isEmpty(html)){
            System.out.println("缓存");
            return html;
        }
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


        model.addAttribute("goods",goods);
        model.addAttribute("state",state); //remainseconds
        model.addAttribute("remainseconds",remainseconds);

        WebContext ctx = new WebContext(request,response,request.getServletContext(),
                response.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }

        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public ResponseResult toDetail(HttpServletRequest request,HttpServletResponse response, User user,Model model,
                           @PathVariable("goodsId")long goodsId){

        GoodsVo goods = goodsService.getById(goodsId);
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int state = 0;
        long remainseconds = 0;

        if(now < start){
            state = 0; //秒杀还没开始
            remainseconds = (int)(start - now)/1000;
        }else if(now < end){
            state = 1; //正在秒杀
            remainseconds = 0;
        }else{
            state = 2; // 秒杀已结束
            remainseconds = -1;
        }

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds((int) remainseconds);
        vo.setState(state);

        return new ResponseResult(Constants.SUCCESS,vo);
    }
}
