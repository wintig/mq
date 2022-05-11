package com.wintig.rocketmq.detail;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;
import java.util.Set;

public class DetailConsumer {

    public static void main(String[] args) throws MQClientException {

        // 指定一个消费组，一个消息可以被多个群组消费
        // 比如你下一个订单，那么这个订单会被发送到交易群组，物流群组，商品群组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("wintig_consumer_detail");
        consumer.setNamesrvAddr("127.0.0.1:9876");

        /**
         * 设置起一次启动是从对队列头开始消息
         * 如果非第一次启动，那么按照上次消费位置继续消费
         *
         * 上次消费偏移量、最大偏移量、最小偏移量
         *
         * CONSUME_FROM_TIMESTAMP：启动时间戳开始消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 消费模式，默认集群
        consumer.setMessageModel(MessageModel.CLUSTERING);

        // 消费者最小线程数
        consumer.setConsumeThreadMin(20);

        // 消费者最大线程数量
        consumer.setConsumeThreadMax(20);

        // 推模式下任务间隔时间，推模式也是基于不断的轮询拉取
        consumer.setPullInterval(0);

        // 推模式下任务拉取的条数，默认32条 一批批拉
        consumer.setPullBatchSize(32);

        // 消息重试次数，-1表示16次
        consumer.setMaxReconsumeTimes(-1);

        // 消息消费超时时间（消息可能阻塞正在使用的线程的最大时间  分钟）
        consumer.setConsumeTimeout(15);

        // 获取消费者对主体分配了哪些队列
        Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues("wintig_consumer_detail");

        // 方法-订阅
        // 基于主体订阅消息，消息过滤使用表达式；只有第一次创建失效
        consumer.subscribe("wintig_consumer_detail", "TagA || TagC || TagD");
        consumer.subscribe("wintig_consumer_detail", MessageSelector.bySql("a between 0 and 3"));
        consumer.subscribe("wintig_consumer_detail", MessageSelector.byTag("TagA|TagC"));
        // 取消订阅
        consumer.unsubscribe("wintig_consumer_detail");


        // 注册监并发事件监听器，多个线程去消费：setConsumeThreadMin
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // 注册顺序消息事件监听器，每个queue有唯一的consumer去消费，
        // 如果每个topic只有4个queue，那么这里只会启动4个线程
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();
        System.out.println("consumer start...");
    }

}
