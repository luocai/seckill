package com.cai.seckill.controller;

import com.cai.seckill.common.Constants;
import com.cai.seckill.common.ResponseResult;
import com.cai.seckill.pojo.User;
import com.cai.seckill.redis.RedisService;
import com.cai.seckill.service.UserService;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/haha")
    @ResponseBody
    public String test(){
        return "hha";
    }

    @RequestMapping("/nice")
    @ResponseBody
    public ResponseResult getId(){
        User user = userService.getById(1);
        System.out.println(user.toString());

        return new ResponseResult(Constants.SUCCESS,"hahhahah",user);
    }

    @RequestMapping("/bili")
    @ResponseBody
    public String haha(){
        redisService.set("jay","这一波naice");

        return redisService.get("jay");
    }
}
