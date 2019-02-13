package com.cai.seckill.dao;

import com.cai.seckill.pojo.Order;
import org.apache.ibatis.annotations.*;


@Mapper
public interface OrderDao {

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    long createOrder(Order order);

    @Select("select * from order_info where user_id=#{userId} and goods_id=#{goodsId}")
    Order getByGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsid);

    @Select("select * from order_info where id = #{orderId}")
    Order getByOrderId(long orderId);
}
