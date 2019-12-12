package com.wintig.rabbitmq.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wintig.rabbitmq.utils.ConnectionUtil;

public class SendMessage {

    // 这里我们定义一个交换机的名字
    private final static String EXCHANGE_NAME = "test_exchange_fanout";


    /**
     * 发布订阅模式这里引申出了一个新的东西“交换机”，“交换机”是个什么玩意？
     * 几个例子，如果我们现在1个消息，需要被多个消费者消费怎么办？从之前的work模式知道了，一个队列即使绑定多消费实例
     * 最终只会被1个消费者消费，带着疑问我来解释这个“交换器”
     *
     * 现在的队列模型不在是以前的一个发布者，一个队列，一个生产者
     * 而是发布者发送消息给了“交换机”，消息经“交换机”达到“队列”，最后“消费者”从队列中获取消息，从而实现了一个消息被多个消费者消费。
     *
     *
     */

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 消息内容
        String message = "Hello World!";

        /**
         * 注意了，消息发送没有队列绑定的交换机时，消息将丢失，因为交换机没有存储消息的能力，消息只能存储在队列里
         */
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }


}
