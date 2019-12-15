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
     *
     * rabbitmq的消息模型是生产者从不将任何消息直接发送到任何队列，取而代之的是生产这将消息发送给交换机。
     * 交换机接受来自生产者的消息，并且将他推送到队列中。根据创建交换机的不同，推送消息的方式也不同
     * 交换机有4类分别是：direct, topic, headers , fanout
     *
     * 这时候队列不再监听生产者的消息，而是对交换机感兴趣。
     *
     */

    public static void main(String[] argv) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        /**
         *
         * 声明exchange，
         *
         * type：交换机类型
         *
         * direct   ： 路由模式，队列可以只获取他感兴趣的内容
         * fanout   ： 接受消息，并且将它广播到所有的队列中
         * topic    ：
         * headers  ：
         *
         */
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
