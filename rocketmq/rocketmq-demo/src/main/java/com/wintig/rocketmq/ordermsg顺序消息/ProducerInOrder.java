package com.wintig.rocketmq.ordermsg顺序消息;

import module.Order;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProducerInOrder {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {

        DefaultMQProducer producer = new DefaultMQProducer("wintig_order_producer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();

        String[] tags = new String[]{"TagA", "TagC", "TagD"};

        // 订单列表
        List<Order> orderList = new ProducerInOrder().buildOrders();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);

        for (int i = 0; i < orderList.size(); i++) {

            String body = dateStr + " Order:" + orderList.get(i);
            Message msg = new Message("wintig_part_order", tags[i % tags.length],
                    "KEY" + i,
                    body.getBytes(StandardCharsets.UTF_8));

            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object arg) {

                    // 根据订单id选择发送queue
                    Long id = (Long) arg;
                    long index = id % list.size();
                    return list.get((int) index);
                }
            }, orderList.get(i).getOrderId());
            System.out.println(sendResult);
        }

        producer.shutdown();
    }

    private List<Order> buildOrders() {

        List<Order> orderList = new ArrayList<>();

        orderList.add(new Order(20210406001L, "创建"));
        orderList.add(new Order(20210406002L, "创建"));

        orderList.add(new Order(20210406001L, "付款"));
        orderList.add(new Order(20210406003L, "创建"));

        orderList.add(new Order(20210406002L, "付款"));
        orderList.add(new Order(20210406003L, "付款"));

        orderList.add(new Order(20210406003L, "推送"));
        orderList.add(new Order(20210406003L, "完成"));

        orderList.add(new Order(20210406002L, "推送"));
        orderList.add(new Order(20210406001L, "推送"));

        orderList.add(new Order(20210406001L, "完成"));
        orderList.add(new Order(20210406002L, "完成"));


        return orderList;
    }

}
