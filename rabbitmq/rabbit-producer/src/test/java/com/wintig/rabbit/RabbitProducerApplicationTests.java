package com.wintig.rabbit;

import com.wintig.rabbit.entity.Order;
import com.wintig.rabbit.producer.OrderSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitProducerApplicationTests {

	@Autowired
	private OrderSender orderSender;

	@Test
	public void testSend1() throws Exception {

		Order order = new Order();
		order.setId("201903310001");
		order.setName("测试");
		order.setMessageID(System.currentTimeMillis() + "$" + UUID.randomUUID());

		orderSender.send(order);
	}



}
