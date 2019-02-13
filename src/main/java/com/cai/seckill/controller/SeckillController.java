package com.cai.seckill.controller;

import com.cai.seckill.access.AccessLimit;
import com.cai.seckill.common.Constants;
import com.cai.seckill.common.ResponseResult;
import com.cai.seckill.pojo.Order;
import com.cai.seckill.pojo.User;
import com.cai.seckill.rabbitmq.MqSender;
import com.cai.seckill.rabbitmq.SeckillMsg;
import com.cai.seckill.redis.keys.GoodsKey;
import com.cai.seckill.redis.RedisService;
import com.cai.seckill.redis.keys.SeckillKey;
import com.cai.seckill.service.GoodsService;
import com.cai.seckill.service.OrderService;
import com.cai.seckill.service.SeckillService;
import com.cai.seckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean{

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MqSender mqSender;

    private Map<Long,Boolean> localOverMap = new HashMap<>();

    @RequestMapping("/{path}/doSeckill")
    @ResponseBody
    public ResponseResult doSeckill(Model model, User user, @RequestParam("goodsId") long goodsId,
                                    @PathVariable("path") String path){


        //未登陆则先登陆
//        if(user == null){
//            return new ResponseResult(Constants.NEED_LOGIN);
//      .  }

        boolean check = seckillService.checkPath(user.getId(),goodsId,path);
        if(!check){
            return new ResponseResult(Constants.REQUEST_ILLEGAL,"非法请求");
        }

        boolean over = localOverMap.get(goodsId);
        if(over) {
            return new ResponseResult(Constants.MIAO_SHA_OVER,"秒杀已经结束");
        }

        //预减库存 代替每次从数据库中查找
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock,""+goodsId);
        System.out.println("stock ：" + stock );
        if(stock < 0){
            localOverMap.put(goodsId,true);
            return new ResponseResult(Constants.MIAO_SHA_OVER,"秒杀已经结束");
        }

        //判断是否重复秒杀
        Order order = orderService.getOrderByGoodsId(user.getId(),goodsId);
        if(order != null){
           return new ResponseResult(Constants.REPEATE_MIAOSHA,"重复秒杀");
        }

        //把请求放入rabbitmq队列中
        SeckillMsg msg = new SeckillMsg();
        msg.setGoodsId(goodsId);
        msg.setUser(user);
        mqSender.send(msg);
        //直接返回结果
        return new ResponseResult(Constants.SUCCESS);
    }

    /*
        原来的秒杀，每次从数据库中查找商品，判断库存，当大量请求时，数据库访问过多，压力大
     */
//    @RequestMapping("/doSeckill")
//    @ResponseBody
//    public ResponseResult doSeckill(Model model, User user, @RequestParam("goodsId") long goodsId){
//
//
////        未登陆则先登陆
//        if(user == null){
//            return new ResponseResult(Constants.NEED_LOGIN);
//        }
//
//        GoodsVo goods = goodsService.getById(goodsId);
//        if(goods == null){
//            return new ResponseResult(Constants.SERVER_ERROR,"该商品不存在");
//        }
//
//        //判断是否秒杀结束
//        if(goods.getStockCount() <= 0){
//           return new ResponseResult(Constants.MIAO_SHA_OVER,"秒杀已经结束");
//        }
//
//        //判断是否重复秒杀
//        Order order = orderService.getOrderByGoodsId(user.getId(),goodsId);
//        if(order != null){
//            return new ResponseResult(Constants.REPEATE_MIAOSHA,"重复秒杀");
//        }
//
//        //进行秒杀（减库存，写入订单）
//        order = secikllService.doSeckill(user,goods);
//
//        return new ResponseResult(Constants.SUCCESS,order);
//    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method= RequestMethod.GET)
    @ResponseBody
    public ResponseResult seckillResult(Model model,User user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
//        if(user == null) {
//            return new ResponseResult(Constants.NEED_LOGIN);
//        }
        long result  = seckillService.getSeckillResult(user.getId(),goodsId);
        System.out.println("result goodsid:" + result);
        return new ResponseResult(Constants.SUCCESS,result);
    }

    /*
    初始化，把所有商品库存放入redis

     */
    @Override
    public void afterPropertiesSet() throws Exception {

        List<GoodsVo> goodsVos = goodsService.listGoodsVo();

        for (GoodsVo goodsVo: goodsVos){
            redisService.set(GoodsKey.getSeckillGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
        }

    }


    /*
    5秒之内最多只能点击五次
     */
    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult path(Model model,User user,@RequestParam("goodsId") long goodsId,
                               @RequestParam(value="verifyCode", defaultValue="0")int verifyCode){

//        if (user == null){
//            return new ResponseResult(Constants.NEED_LOGIN,"未登录");
//        }
        Integer vcode = redisService.get(SeckillKey.getMiaoshaVerifyCode,""+user.getId()+"_"+goodsId,Integer.class);

        if(vcode == null || vcode != verifyCode){
            return new ResponseResult(Constants.verifyCode_ERROR,"验证码错误");
        }

        String path = seckillService.createSeckillPath(user.getId(),goodsId);
        return new ResponseResult(Constants.SUCCESS,path);
    }

    /*
    验证码请求
     */
    @RequestMapping(value="/verifyCode", method=RequestMethod.GET)
    @ResponseBody
    public ResponseResult getMiaoshaVerifyCod(HttpServletResponse response, User user,
                                              @RequestParam("goodsId")long goodsId) {
        if(user == null) {
            return new ResponseResult(Constants.NEED_LOGIN,"未登录");
        }
        try {
            BufferedImage image  = seckillService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseResult(Constants.SERVER_ERROR,"服务器异常");
        }
    }
}
