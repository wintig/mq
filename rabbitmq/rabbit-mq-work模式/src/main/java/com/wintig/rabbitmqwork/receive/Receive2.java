package com.wintig.rabbitmqwork.receive;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.wintig.rabbitmqwork.utils.ConnectionUtil;

/**
 * 第一个消费者
 */
public class Receive2 {

    private final static String QUEUE_NAME = "test_queue_work";

    public static void main(String[] argv) throws Exception {

        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //channel.basicQos(1);


        // 定义队列的消费者
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");

            // 休眠1秒
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };


        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

    }


}
