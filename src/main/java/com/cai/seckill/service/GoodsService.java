package com.cai.seckill.service;

import com.cai.seckill.dao.GoodsDao;
import com.cai.seckill.pojo.Goods;
import com.cai.seckill.pojo.SeckillGoods;
import com.cai.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getById(long goodsId) {
        return goodsDao.getById(goodsId);
    }
    public int reducestock(GoodsVo goods){
        SeckillGoods sgoods = new SeckillGoods();
        sgoods.setGoodsId(goods.getId());
        return goodsDao.reducestock(sgoods);
    }
}
