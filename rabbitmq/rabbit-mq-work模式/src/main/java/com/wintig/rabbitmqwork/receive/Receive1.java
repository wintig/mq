package com.wintig.rabbitmqwork.receive;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.wintig.rabbitmqwork.utils.ConnectionUtil;

/**
 * 第一个消费者
 */
public class Receive1 {

    private final static String QUEUE_NAME = "test_queue_work";

    /**
     *
     * 这时候，如果有2个channel监听一个消息队列
     *
     * 那么这时候rabbit会采用轮询的方式分发消息，这就是我们会看到消费者1和消费者2呈现奇数偶数交替打印。
     *
     * Receive1的消费能力明明比Receive2的消费能力强（sleep模拟），但是Receive1为了等待Receive2就显得效率差不读
     *
     *
     *
     * 轮询分发：再默认情况下，rabbitMQ将逐个发送消息再序列中的下一个消费者（不考虑每个人物的时长，且是一次性分配，并非一个一个分配）
     * 平均每个消费者获得相同数量的消息
     *
     * 公平分发：轮询分发 虽然看起来很不错，但是有个问题：例如2个消费者，奇数的消息都是繁忙的，而偶数则是轻松的。按照轮询的方式，奇数的任务
     * 交给了第一个消费者，所以一直再忙个不停。偶数的任务交给另一个消费者，则立刻完成任务然后闲的不行。而rabbitMQ则是不了解这些的。
     * 这是因为当消息进入队列，RabbitMQ就会分派消息。它不看消费者相应情况，而是盲目的将消息轮询指定给消费者。
     * 为了解决这个问题，可以使用basicQos( prefetchCount = 1)方法，来限制：RabbitMQ只发不超过1条消息给同一个消费者。当消息处理完毕有反馈后，才发送第二次
     *
     * 还有一点需要注意，使用公平分发，必须关闭自动应答，改为手动应答。
     */


    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        // 定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        /**
         * 消息的确认模式分两种
         * 如果这里是true的话，无论消费者获取到消息成功后是否成功消费，都认为是已经成功消费了
         *
         * 手动确认：消费者从队列中获取消息后，服务器会将该消息标记为不可用状态，等待消费者反馈，如果消费者一直没有反馈
         * 那么消息将一直处于不可用
         *
         * 监听队列，false表示手动返回完成状态，true表示自动
         *
         */
        channel.basicConsume(QUEUE_NAME, false, consumer);

        // 获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [y] Received '" + message + "'");

            //休眠
            Thread.sleep(10);

            // 返回确认状态，注释掉表示使用自动确认模式
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }



}
