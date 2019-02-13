package com.cai.seckill.controller;

import com.cai.seckill.common.Constants;
import com.cai.seckill.common.ResponseResult;
import com.cai.seckill.pojo.User;
import com.cai.seckill.service.UserService;
import com.cai.seckill.util.MD5Util;
import com.cai.seckill.util.UUIDUtil;
import com.cai.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String toLogin(){
        return "login";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult login(HttpServletResponse response, @Valid LoginVo loginVo){

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //登录
        String token = userService.login(response,mobile,password);

        return new ResponseResult(Constants.SUCCESS,token);
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String toRegister(){
        return "register";
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult register(HttpServletResponse response, @Valid LoginVo loginVo){

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        User user = userService.getByMobile(mobile);
        if(user != null){
            return new ResponseResult(Constants.MOBILE_HAS_BEREGISTERED,"该手机号已经被注册");
        }

        user = new User();
        user.setMobile(mobile);
        user.setSalt(UUIDUtil.uuid());
        user.setPassword(MD5Util.inputPassToDbPass(password,user.getSalt()));
        user.setRegisterDate(new Date());
        userService.register(user);

        return new ResponseResult(Constants.SUCCESS);
    }
}
