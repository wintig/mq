package com.wintig.rabbitmqwork;

import com.wintig.rabbitmqwork.boot_sender.HelloSender;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class RabbitMqWork模式ApplicationTests {

	@Autowired
	private HelloSender helloSender;

	@Test
	public void hello() {

		for (int i = 0; i < 100; i++) {
			helloSender.send(i);
		}

	}
}
