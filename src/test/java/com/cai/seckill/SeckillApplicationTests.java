package com.cai.seckill;

import com.cai.seckill.dao.OrderDao;
import com.cai.seckill.pojo.Order;
import com.cai.seckill.rabbitmq.MqSender;
import com.cai.seckill.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeckillApplicationTests {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private MqSender sender;

	@Test
	public void contextLoads() {
//		sender.send("hello");
		Order order = orderService.getOrderByGoodsId(0,1);
		Order order1 = orderDao.getByGoodsId(0,1);
		System.out.println(order);
		System.out.println("hhh");
	}

	@Test
	public void Recieve() {
//		sender.send("hello");
		System.out.println("hhh");
	}

}

