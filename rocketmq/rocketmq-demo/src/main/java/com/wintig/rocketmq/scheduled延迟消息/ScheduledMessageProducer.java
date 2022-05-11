package com.wintig.rocketmq.scheduled延迟消息;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class ScheduledMessageProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException {

        DefaultMQProducer producer = new DefaultMQProducer("ScheduledProducer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message msg = new Message("scheduled_topic",
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            // 设置延迟等级3，这个消息将在10S后投递给消费者
            // delayTimeLevel : 1 5 10 30 1m 2m 3m 4m ....10m 20m 30m 1h 2h
            msg.setDelayTimeLevel(3);
            producer.sendOneway(msg);
        }

        producer.shutdown();

    }
}
