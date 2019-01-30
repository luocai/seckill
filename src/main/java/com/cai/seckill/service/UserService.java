package com.cai.seckill.service;

import com.cai.seckill.dao.UserDao;
import com.cai.seckill.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getById(Integer id){

        return userDao.getById(id);
    }
}
