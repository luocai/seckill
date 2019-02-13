package com.cai.seckill.redis.keys;

import com.cai.seckill.redis.prefix.BasePrefix;

public class UserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600*24 * 2;

    public UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey token = new UserKey(TOKEN_EXPIRE, "tk");
    public static UserKey getById = new UserKey(0, "id");


}
