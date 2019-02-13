package com.cai.seckill.vo;

import com.cai.seckill.pojo.User;


public class GoodsDetailVo {
	private int state = 0;
	private int remainSeconds = 0;
	private GoodsVo goods ;
	private User user;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getRemainSeconds() {
		return remainSeconds;
	}

	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}

	public GoodsVo getGoods() {
		return goods;
	}

	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
