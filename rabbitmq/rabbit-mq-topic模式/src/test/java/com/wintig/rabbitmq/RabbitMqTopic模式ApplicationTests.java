package com.wintig.rabbitmq;

import com.wintig.rabbitmq.boot_sender.HelloSender;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class RabbitMqTopic模式ApplicationTests {

	@Autowired
	private HelloSender helloSender;

	@Test
	public void send1() throws Exception {
		helloSender.send1();
	}

	@Test
	public void send2() throws Exception {
		helloSender.send2();
	}


}
