package com.cai.seckill.dao;

import com.cai.seckill.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {


    void insert(User user);

    void update(User user);

    @Select("select * from user where id = #{id}")
    User getById(Integer id);

    void deleteById(Integer id);
}
