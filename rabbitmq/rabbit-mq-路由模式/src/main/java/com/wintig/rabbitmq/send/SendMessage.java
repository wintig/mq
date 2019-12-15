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
         * 消息广播的交换机缺少一定的灵活性，只能进行无意识的广播。
         * 这时候我们可以使用“direct exchange”交换机，这时候我们可以吧消息赋予某种属性
         * 每一条消息都绑定了一个routingKey，队列只会接受到和自己绑定的routingKey的消息
         *
         */
        channel.basicPublish(EXCHANGE_NAME, "delete", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }


}
