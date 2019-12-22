package com.wintig.rabbitmq.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wintig.rabbitmq.utils.ConnectionUtil;

import java.util.concurrent.TimeUnit;

public class SendMessage {

    private final static String QUEUE_NAME = "test_transactional_message";



    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();

        // 从连接中创建通道
        Channel channel = connection.createChannel();

        // 声明（创建）队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 消息内容
        String message = "Hello World!";

        try {
            // 开启事物
            channel.txSelect();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            TimeUnit.SECONDS.sleep(3);

            // 发生了异常
            int i = 1 / 0;

            System.out.println("消息成功提交");
            // 提交事物
            channel.txCommit();
        } catch (Exception e) {
            System.out.println("消息回滚了");
            // 回滚事务
            channel.txRollback();
        }
        System.out.println(" [x] Sent '" + message + "'");

        //关闭通道和连接
        channel.close();
        connection.close();
    }


}
