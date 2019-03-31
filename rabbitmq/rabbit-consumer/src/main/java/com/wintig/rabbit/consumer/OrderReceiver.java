package com.wintig.rabbit.consumer;

import com.rabbitmq.client.Channel;
import com.wintig.rabbit.entity.Order;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OrderReceiver {

    /**
     * @RabbitListener 消息监听，可配置交换机、队列、路由key，该注解会创建队列和交互机 并建立绑定关系
     * @RabbitHandler 标识此方法如果有消息过来，消费者要调用这个方法
     * @Payload 消息体
     * @Headers 消息头
     * @param order
     */
    @RabbitListener(bindings = @QueueBinding(
                value = @Queue(value = "order-queue", declare = "true"),
                exchange = @Exchange(name = "order-exchange", durable = "true", type = "topic"),
                key = "order.*"
            )
    )
    @RabbitHandler
    public void onOrderMessage(@Payload Order order,
                               @Headers Map<String, Object> headers,
                               Channel channel) throws IOException {

        // 消费者操作
        System.out.println("订单ID：" + order.getId() + "收到消息");


        /**
         * ACL
         * 如果我们设置了手工签收模式spring.rabbitmq.listener.simple.acknowledge-mode=manual
         * 而没有调用这个方法的话，那么消息就永远不会被消费了成功，留在那里
         */
        channel.basicAck((Long) headers.get(AmqpHeaders.DELIVERY_TAG),false);

    }

}
