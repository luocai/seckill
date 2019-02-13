package com.cai.seckill.service;

import com.cai.seckill.pojo.Order;
import com.cai.seckill.pojo.User;
import com.cai.seckill.redis.RedisService;
import com.cai.seckill.redis.keys.SeckillKey;
import com.cai.seckill.util.MD5Util;
import com.cai.seckill.util.UUIDUtil;
import com.cai.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
public class SeckillService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public Order doSeckill(User user, GoodsVo goods){
        //减库存
        boolean success = goodsService.reducestock(goods);

        if(success){
            //生成订单项进入数据库中
            Order order = orderService.createOrder(user, goods);
            return order;
        }else {
            //如果减库存失败，说明没库存了，秒杀失败，设置标志位
            setOverFlag(goods.getId());
            return null;
        }

    }



    public long getSeckillResult(long userId,long goodsId){
        Order order = orderService.getOrderByGoodsId(0,goodsId);

        //订单不为空说明秒杀成功，返回商品id
        if(order != null){
            return goodsId;
        }else {
            //订单为空有两种情况，一种是没库存了，另外一种是还在队列中排队处理，要分开处理
            boolean over = getOverFlag(goodsId);
            if(over == true){
                return -1;
            }else {
                return 0;
            }
        }
    }

    //用来判断秒杀是结束了还是在排队
    private void setOverFlag(Long id) {
        redisService.set(SeckillKey.getGoodsOverFlag,""+id,true);
    }

    private boolean getOverFlag(long goodsId) {
        return redisService.exists(SeckillKey.getGoodsOverFlag,""+goodsId);
    }

    //随机创建地址
    public String createSeckillPath(Long userId, long goodsId) {

        String path = MD5Util.md5(UUIDUtil.uuid()+"123456");

        redisService.set(SeckillKey.getSeckillPath,""+userId+"_"+goodsId,path);

        return path;
    }

    //验证地址
    public boolean checkPath(Long userId, long goodsId, String path) {

        if(redisService.get(SeckillKey.getSeckillPath,""+userId+"_"+goodsId,String.class).equals(path)){
            return true;
        }else{
            return false;
        }
    }

    //生成验证码
    public BufferedImage createVerifyCode(User user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SeckillKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    //验证验证码是否正确
    public boolean checkVerifyCode(User user, long goodsId, int verifyCode) {
        if(user == null || goodsId <=0) {
            return false;
        }
        Integer codeOld = redisService.get(SeckillKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, Integer.class);
        if(codeOld == null || codeOld - verifyCode != 0 ) {
            return false;
        }
        redisService.delete(SeckillKey.getMiaoshaVerifyCode, user.getId()+","+goodsId);
        return true;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }
}
