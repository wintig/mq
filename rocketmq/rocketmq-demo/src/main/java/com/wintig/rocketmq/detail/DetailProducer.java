package com.wintig.rocketmq.detail;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DetailProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {

        DefaultMQProducer producer = new DefaultMQProducer("wintig_produce_details");
        producer.setNamesrvAddr("127.0.0.1:9876");

        // 一个topic在每个broker队列数列（对于新创建有效）
        producer.setDefaultTopicQueueNums(8);

        // 发送消息默认超时时间
        producer.setSendMsgTimeout(1000 * 3);

        // 消息体超过该值，则启用压缩，默认4k
        producer.setCompressMsgBodyOverHowmuch(1024 * 4);

        // "同步方式", 发送消息重试次数，默认2，工执行3次
        producer.setRetryTimesWhenSendFailed(2);

        // 消息重试时选择另外一个broker时（消息发送失败，没有存储成功是否发送到另外一个broker），默认为false
        producer.setRetryAnotherBrokerWhenNotStoreOK(false);

        // 允许发送的最大消息长度
        producer.setMaxMessageSize(1024 * 1024 * 4);

        producer.start();

        // 查找该主题下所有消息队列
        List<MessageQueue> messageQueues = producer.fetchPublishMessageQueues("wintig_produce_details_test");

        for (int i = 0; i < 100; i++) {
            final int index = i;

            Message msg = new Message("TopicTest",
                    "TagA", "OrderId-777",
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            // 单向发送
            // 1.1
            producer.sendOneway(msg);
            // 1.2 指定队列单向发送消息
            producer.sendOneway(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    return mqs.get(0);
                }
            }, null);
            // 1.3指定队列单向发送消息，根据之前查找出来的主题(1.2的简化)
            producer.sendOneway(msg, messageQueues.get(0));


            // 同步发送
            // 2.1
            producer.send(msg);
            // 2.2 同步超时发送消息, 默认3s
            producer.send(msg, 1000 * 3);
            // 2.3 指定队列
            producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    return mqs.get(0);
                }
            }, null);
            // 2.4 指定队列同步发送消息， 2.3的简化
            producer.send(msg, messageQueues.get(0));

            // 异步发送
            // 3.1
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {

                }

                @Override
                public void onException(Throwable e) {

                }
            }, 1000 * 3);
            // 3.2选择队列发送消息
            producer.send(msg, messageQueues.get(0), new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {

                }

                @Override
                public void onException(Throwable e) {

                }
            }, 1000 * 3);
            // 3.3 选择队列
            producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    return mqs.get(0);
                }
            },
                    new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {

                }

                @Override
                public void onException(Throwable e) {

                }
            });
        }

        TimeUnit.SECONDS.sleep(5);
        producer.shutdown();
    }

}
