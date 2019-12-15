package com.wintig.rabbitmq.receive;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.wintig.rabbitmq.utils.ConnectionUtil;

public class Receive2 {

    private final static String QUEUE_NAME = "test_queue_direct_2";

    private final static String EXCHANGE_NAME = "test_exchange_direct";

    public static void main(String[] argv) throws Exception {


        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定队列到交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "insert");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "update");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "delete");

        channel.basicQos(1);


        // 定义队列的消费者
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [Receive2] Receive2 '" + message + "'");
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
