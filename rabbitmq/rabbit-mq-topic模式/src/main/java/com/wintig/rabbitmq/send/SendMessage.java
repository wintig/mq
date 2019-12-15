package com.wintig.rabbitmq.send;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wintig.rabbitmq.utils.ConnectionUtil;

public class SendMessage {


    private final static String EXCHANGE_NAME = "test_exchange_topic";

    /**
     * topic模式是rabbitmq中最灵活的一种，可以认为是路由模式的高级版
     * 之前路由模式的话，只能根据一个很确定的“route key”来匹配
     * topic模式下可以进行通配符的设置
     *
     * *：可以代替一个单词
     * #：可以替代零个或多个单词
     *
     * order.abcd 是唯一指定
     * order.*    可以匹配order.1234 , order.5678 但是不能匹配order.1234.5678
     * order.#    可以匹配以order开头的任意规则
     *
     * 如果一个消费者匹配成功了2个key，消息只会发送1次；如果消息没有和任何一个队列匹配，那么则会丢弃。
     *
     */

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // 消息内容
        String message = "Hello World!!";
        channel.basicPublish(EXCHANGE_NAME, "route-key.1", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }



}
