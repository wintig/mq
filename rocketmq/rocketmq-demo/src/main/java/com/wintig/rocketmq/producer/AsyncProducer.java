package com.wintig.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class AsyncProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {

        DefaultMQProducer producer = new DefaultMQProducer("wintig_one_way_group");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();

        producer.setRetryTimesWhenSendAsyncFailed(0);
        producer.setSendLatencyFaultEnable(true);

        for (int i = 0; i < 100; i++) {
            final int index = i;

            Message msg = new Message("wintig_topic_test",
                    "TagA",
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            // 异步接受消息回调通知
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(index + " OK " + sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println(index + " ERROR " + throwable);
                }
            });
        }

        Thread.sleep(100000);
        producer.shutdown();
    }

}
