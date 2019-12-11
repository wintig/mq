package com.wintig.rabbitmq.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wintig.rabbitmq.utils.ConnectionUtil;

public class SendMessage {

    private final static String QUEUE_NAME = "test_queue_1";


    /**
     *
     * 简单队列模式，相当于Hello World吧，最简单的一种消息类型
     *
     * 一个客户端把消息发到队列，然后一个消费者进行消费
     *
     */

    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();

        // 从连接中创建通道
        Channel channel = connection.createChannel();

        // 声明（创建）队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 消息内容
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        //关闭通道和连接
        channel.close();
        connection.close();
    }


}
