package com.wintig.rabbitmq.receive;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.wintig.rabbitmq.utils.ConnectionUtil;

public class Receive1 {

    private final static String QUEUE_NAME = "test_queue_topic_work_1";
    private final static String EXCHANGE_NAME = "test_exchange_topic";


    public static void main(String[] argv) throws Exception {


        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定队列到交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "route-key.*");
        channel.basicQos(1);

        // 定义队列的消费者
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [Receive1] Received1 '" + message + "'");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        };


        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }



}
