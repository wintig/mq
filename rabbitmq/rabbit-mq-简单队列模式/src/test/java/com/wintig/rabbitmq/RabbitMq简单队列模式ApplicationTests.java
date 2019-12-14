package com.wintig.rabbitmq;

import com.wintig.rabbitmq.boot_sender.HelloSender;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class RabbitMq简单队列模式ApplicationTests {

	@Autowired
	private HelloSender helloSender;

	@Test
	public void hello() {
		helloSender.send();
	}

}
