package com.cai.seckill.dao;

import com.cai.seckill.pojo.Goods;
import com.cai.seckill.pojo.SeckillGoods;
import com.cai.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on g.id = mg.goods_id ")
    List<GoodsVo> listGoodsVo();

    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on g.id = mg.goods_id where goods_id = #{id}")
    GoodsVo getById(long id);

    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} " )
    int reducestock(SeckillGoods goods);
}
