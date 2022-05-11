package com.wintig.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class OneWayProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException {

        // 实例化消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("wintig_one_way_group");

        // 设置nameserver的地址
        producer.setNamesrvAddr("127.0.0.1:9876");

        // 启动producer实例
        producer.start();

        for (int i = 0; i < 10; i++) {
            // 创建消息，并指定topic、 tag、 和消息体
            Message msg = new Message("wintig_topic_test",
                    "TagA",
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            // 单向消息，没有任何返回结果
            producer.sendOneway(msg);
        }

        // 如果不再发送消息，关闭producer实例
        producer.shutdown();

    }

}
