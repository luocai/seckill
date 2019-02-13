package com.cai.seckill.service;

import com.cai.seckill.common.CodeMsg;
import com.cai.seckill.common.Constants;
import com.cai.seckill.dao.UserDao;
import com.cai.seckill.exception.GlobalException;
import com.cai.seckill.pojo.User;
import com.cai.seckill.redis.RedisService;
import com.cai.seckill.redis.keys.UserKey;
import com.cai.seckill.util.MD5Util;
import com.cai.seckill.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    UserDao userDao;

    @Autowired
    RedisService redisService;

    public User getById(long id) {
        //取缓存
        User user = redisService.get(UserKey.getById, ""+id, User.class);
        if(user != null) {
            return user;
        }
        //取数据库
        user = userDao.getById(id);
        if(user != null) {
            redisService.set(UserKey.getById, ""+id, user);
        }
        return user;
    }
    // http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
//    public boolean updatePassword(String token, long id, String formPass) {
//        //取user
//        MiaoshaUser user = getById(id);
//        if(user == null) {
//            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
//        }
//        //更新数据库
//        MiaoshaUser toBeUpdate = new MiaoshaUser();
//        toBeUpdate.setId(id);
//        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
//        miaoshaUserDao.update(toBeUpdate);
//        //处理缓存
//        redisService.delete(MiaoshaUserKey.getById, ""+id);
//        user.setPassword(toBeUpdate.getPassword());
//        redisService.set(MiaoshaUserKey.token, token, user);
//        return true;
//    }


    public User getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        //延长有效期
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }


    public String login(HttpServletResponse response, String mobile,String password) {

        //判断手机号是否存在
//        User user = getById(Long.parseLong(mobile));
        User user = userDao.getByMobile(mobile);
        if(user == null) {
            throw new GlobalException(Constants.MOBILE_NOT_EXIST,"手机号不存在");
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(password, saltDB);
        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(Constants.verifyCode_ERROR,"密码错误");
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public User getByMobile(String mobile){
        return userDao.getByMobile(mobile);
    }

    public void register(User user) {
        userDao.addUser(user);
    }
}
