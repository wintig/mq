package com.wintig.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.wintig")
public class RabbitMq简单队列模式Application {

	public static void main(String[] args) {
		SpringApplication.run(RabbitMq简单队列模式Application.class, args);
	}

}
