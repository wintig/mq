package com.wintig.rocketmq.ordermsg顺序消息;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ConsumerOrder {

    public static void main(String[] args) throws MQClientException {

        // 指定一个消费组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("order_consumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");

        /**
         * 设置起一次启动是从对队列头开始消息
         * 如果非第一次启动，那么按照上次消费位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("wintig_part_order", "*"/*"TagA || TagC || TagD"*/);

        // 注册回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerOrderly() {

            Random random = new Random();

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext context) {
                context.setAutoCommit(true);

                for (MessageExt msg : list) {
                    // 可以看到每个queue有唯一的consumer线程来消费，订单对每个queue有序
                    System.out.println("consumerThread = " + Thread.currentThread().getName()
                            + ", tag: " + msg.getTags()
                            + ", queueId: " + msg.getQueueId()
                            + ", content:" + new String(msg.getBody()));
                }

                try {
                    // 模拟业务处理
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(300));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 等一会再处理这批消息，而不是放到重试队列
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }

                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();
        System.out.println("consumer start...");
    }

}
