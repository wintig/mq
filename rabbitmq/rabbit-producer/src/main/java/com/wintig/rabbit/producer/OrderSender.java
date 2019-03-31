package com.wintig.rabbit.producer;


import com.wintig.rabbit.entity.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Order order) throws Exception {

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getMessageID());

        rabbitTemplate.convertAndSend("order-exchange",
                "order.abcd",       // 路由key
                order,
                correlationData     //消息唯一id
                );
    }

}
