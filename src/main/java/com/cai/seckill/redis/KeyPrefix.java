package com.cai.seckill.redis;

/*
  键前缀接口
 */
public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();
}
