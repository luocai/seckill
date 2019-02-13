package com.cai.seckill.common;

public class Constants {

    //通用的错误码
    public static Integer SUCCESS = 200  ;  // "success"
    public static Integer NEED_LOGIN = 401; // 未登录


    public static Integer SERVER_ERROR = 500100;// "服务端异常");
    public static Integer BIND_ERROR = 500101;/// "参数校验异常：%s");

    //订单模块
    public static Integer ORDER_NOT_EXIST; //订单不存在

    public static Integer REQUEST_ILLEGAL = 500102; //非法请求

    public static Integer verifyCode_ERROR = 50000; //验证码错误

    public static Integer ACCESS_LIMIT_REACHED= 500104; //"访问太频繁！"
    //登录模块 5002XX
    public static Integer SESSION_ERROR = 500210; //"Session不存在或者已经失效");
    public static Integer PASSWORD_EMPTY = 500211; //"登录密码不能为空");
    public static Integer MOBILE_EMPTY = 500212; //"手机号不能为空");
    public static Integer MOBILE_ERROR = 500213; //"手机号格式错误");
    public static Integer MOBILE_NOT_EXIST = 500214;// "手机号不存在");
    public static Integer PASSWORD_ERROR = 500215;// "密码错误");

    public static Integer MOBILE_HAS_BEREGISTERED = 500216;// "该手机号已被注册"

    //商品模块 5003XX

    //订单模块 5004XX

    //秒杀模块 5005XX
    public static Integer MIAO_SHA_OVER = 500500; //"商品已经秒杀完毕");
    public static Integer REPEATE_MIAOSHA = 500501; //"不能重复秒杀");
}
