package com.cai.seckill.redis.keys;

import com.cai.seckill.redis.prefix.BasePrefix;

public class SeckillKey extends BasePrefix {

    public SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static SeckillKey getGoodsOverFlag = new SeckillKey(60, "go");

    public static SeckillKey getSeckillPath = new SeckillKey(60, "pa");

    public static SeckillKey getMiaoshaVerifyCode = new SeckillKey(60, "mv");
}
