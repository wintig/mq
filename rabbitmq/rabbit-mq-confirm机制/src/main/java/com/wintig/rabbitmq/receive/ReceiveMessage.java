package com.wintig.rabbitmq.receive;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.wintig.rabbitmq.utils.ConnectionUtil;


public class ReceiveMessage {

    private final static String QUEUE_NAME = "test_confirm_message";

    public static void main(String[] argv) throws Exception {


        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 定义队列的消费者
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");

        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });


    }



}
