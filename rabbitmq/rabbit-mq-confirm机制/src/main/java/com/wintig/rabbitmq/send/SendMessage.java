package com.wintig.rabbitmq.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.wintig.rabbitmq.utils.ConnectionUtil;

import java.io.IOException;

public class SendMessage {

    private final static String QUEUE_NAME = "test_confirm_message";


    public static void main(String[] argv) throws Exception {

        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //指定我们的消息投递模式: 消息的确认模式
        channel.confirmSelect();


        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());


        /**
         * 添加一个确认监听，当rabbitmq处理完成之后会过来回调
         */
        channel.addConfirmListener(new ConfirmListener() {

            // 消息发送失败了，这里我们可以重试发送
            @Override
            public void handleNack(long deliveryTag, boolean multiple) {
                System.err.println("no ack!");
            }

            // 收到ack了
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("ack!");
            }
        });


        System.out.println(" [x] Sent '" + message + "'");

        //关闭通道和连接
        channel.close();
        connection.close();
    }


}
