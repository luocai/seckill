package com.cai.seckill.common;

public class Constants {

    //通用的错误码
    public static Integer SUCCESS = 0  ;  // "success"
    public static Integer SERVER_ERROR = 500100;// "服务端异常");
    public static Integer BIND_ERROR = 500101;/// "参数校验异常：%s");
    //登录模块 5002XX
    public static Integer SESSION_ERROR = 500210; //"Session不存在或者已经失效");
    public static Integer PASSWORD_EMPTY = 500211; //"登录密码不能为空");
    public static Integer MOBILE_EMPTY = 500212; //"手机号不能为空");
    public static Integer MOBILE_ERROR = 500213; //"手机号格式错误");
    public static Integer MOBILE_NOT_EXIST = 500214;// "手机号不存在");
    public static Integer PASSWORD_ERROR = 500215;// "密码错误");

    //商品模块 5003XX

    //订单模块 5004XX

    //秒杀模块 5005XX
    public static Integer MIAO_SHA_OVER = 500500; //"商品已经秒杀完毕");
    public static Integer REPEATE_MIAOSHA = 500501; //"不能重复秒杀");
}
