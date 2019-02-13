package com.cai.seckill.redis;


import com.cai.seckill.redis.prefix.KeyPrefix;
import com.cai.seckill.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    public String get(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public void set(String key, String value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key,value);
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    /**
     * 获取当个对象
     * */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            String  str = jedis.get(realKey);
//            T t =  stringToBean(str, clazz);
            T t = BeanUtil.stringToBean(str,clazz);

            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置对象
     * */
    public <T> boolean set(KeyPrefix prefix, String key,  T value) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            String str = BeanUtil.beanToString(value);
            if(str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            int seconds =  prefix.expireSeconds();
            if(seconds <= 0) {
                jedis.set(realKey, str);
            }else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     * */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加值
     * */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     * */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除
     * */
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            long ret =  jedis.del(realKey);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }

//    private <T> String beanToString(T value) {
//        if(value == null) {
//            return null;
//        }
//        Class<?> clazz = value.getClass();
//        if(clazz == int.class || clazz == Integer.class) {
//            return ""+value;
//        }else if(clazz == String.class) {
//            return (String)value;
//        }else if(clazz == long.class || clazz == Long.class) {
//            return ""+value;
//        }else {
//            return JSON.toJSONString(value);
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private <T> T stringToBean(String str, Class<T> clazz) {
//        if(str == null || str.length() <= 0 || clazz == null) {
//            return null;
//        }
//        if(clazz == int.class || clazz == Integer.class) {
//            return (T)Integer.valueOf(str);
//        }else if(clazz == String.class) {
//            return (T)str;
//        }else if(clazz == long.class || clazz == Long.class) {
//            return  (T)Long.valueOf(str);
//        }else {
//            return JSON.toJavaObject(JSON.parseObject(str), clazz);
//        }
//    }

    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }

}
