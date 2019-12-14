package com.wintig.rabbitmqwork.boot_sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class HelloSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(int i) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//24小时制
        String context = "hello " + i + " " + date;
        System.out.println("Sender : " + context);
        //简单对列的情况下routingKey即为Q名
        this.rabbitTemplate.convertAndSend("q_hello", context);
    }


}
