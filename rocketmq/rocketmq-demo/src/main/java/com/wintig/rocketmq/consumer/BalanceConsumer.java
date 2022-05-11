package com.wintig.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class BalanceConsumer {

    // 集群消费，一条消息同一个分组只会被处理一次
    // 如果创建了多个group，就会被多次消费
    public static void main(String[] args) throws MQClientException {

        // 指定一个消费组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("wintig");
        consumer.setNamesrvAddr("127.0.0.1:9876");

        consumer.setMaxReconsumeTimes(1);

        // 订阅topic，可以对tag进行过滤  tagA  或者 tagA|tagB|tagC
        consumer.subscribe("wintig_topic_test", "*");

        // 负载均衡模式消费
        consumer.setMessageModel(MessageModel.CLUSTERING);

        // 注册回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

                try {
                    for (MessageExt msg : list) {
                        String topic = msg.getTopic();
                        String msgBody = new String(msg.getBody(), "utf-8");
                        String tags = msg.getTags();
                        System.out.println("收到消息：" + msgBody + " topic : " + topic + " tag : " + tags);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    // 消息消费失败
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

                // 提交消息消费成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.println("consumer start...");
    }

}
