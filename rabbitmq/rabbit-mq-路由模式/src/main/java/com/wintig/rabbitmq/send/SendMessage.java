package com.wintig.rabbitmq.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.wintig.rabbitmq.utils.ConnectionUtil;

public class SendMessage {

    private final static String EXCHANGE_NAME = "test_exchange_direct";


    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // 消息内容
        String message = "删除商品";

        /**
         * 注意了，这里多了一个“routingKey”路由key
         *
         * 路由key是干什么的呢？
         * 当一个交换机绑定了多个消费实例的时候，我这时候生产了一个消息，意思是删除一个商品
         * 但是我绑定了2个消费实例，一个消费实例是“商品后台”，一个消费实例是“订单系统”。
         * 这两个消费者同时监听我这个订单交换机，这时候我发布了一个删除一个商品，我只想让这个消息被“商品后台”知道，不想让“订单系统”收到
         * 这时候怎么办呢？就通过这个路由key吧
         *
         * 我们给消息赋予了某种属性，同时消费者也可以只监听某种消息类型。这样就更能提升工作效率
         *
         */
        channel.basicPublish(EXCHANGE_NAME, "delete", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }


}
