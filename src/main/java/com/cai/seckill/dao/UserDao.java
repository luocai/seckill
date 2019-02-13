package com.cai.seckill.dao;

import com.cai.seckill.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {


    @Select("select * from miaosha_user where id = #{id}")
    public User getById(@Param("id")long id);

    @Update("update miaosha_user set password = #{password} where id = #{id}")
    public void update(User toBeUpdate);

    @Select("select * from miaosha_user where mobile = #{mobile}")
    User getByMobile(String mobile);

    @Insert("insert into miaosha_user(password,salt,register_date,mobile) values (#{password}, #{salt},#{registerDate},#{mobile})")
    void addUser(User user);
}
