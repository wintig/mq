package com.wintig.rabbitmqwork.receive;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.wintig.rabbitmqwork.utils.ConnectionUtil;

/**
 * 第一个消费者
 */
public class Receive1 {

    private final static String QUEUE_NAME = "test_queue_work";

    /**
     *
     * 消息队列的扩展也是很轻松的，简单队列模式下如果1个消费者处理不过来，需要新增消费者来处理的话，直接加入一个队列就好了。
     *
     * 这里我们已经有2个消费者监听了同一个队列，这时候rabbitmq是如何来推送消息呢？
     * 默认情况下，rabbitmq采用轮询分发消息的策略，会将消息一开始就均匀的分给队列中的所有消费者，rabbitmq收到消息的那一刻就决定了这条消息属于哪个worker
     * 每台worker收到的消息数量必定是相等的。rabbitmq不管你的消费能力而是一股脑的分给你。
     *
     * 这会造成一个什么问题呢：例如2个消费者，奇数的消息都是繁忙的，而偶数则是轻松的。按照轮询的方式，奇数的任务交给了第一个消费者，所以一直再忙个不停。
     * 偶数的任务交给另一个消费者，则立刻完成任务然后闲的不行。而rabbitmq则是不了解这些的。
     *
     * 所以这种“公平分发”的策略明显不能适用于我们的项目，我们应该采用“能者多劳”
     *
     *
     * 为了解决这个问题，可以修改channel.basicQos(1);
     * 就是说告诉rabbitmq不要给worker1个以上的消息，换句话说如果worker没有处理完一条消息返回给你ack，那么就别给他发了，而是发给下一个不忙的worker；
     *
     * 如果所有的worker都很忙的话，当前rabbitmq就处于饱和状态。这一点需要注意，增加更多的workers或者采取一些其他措施。
     *
     *
     */


    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        /**
         *
         * 声明队列
         *
         * durable：是否持久化
         *
         * 即使将队列标记为持久化队列，还是会丢失消息的。从rabbitmq接收消息到保存到磁盘这之间还是有一段时间的
         *
         *
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 同一时刻服务器只会发一条消息给消费者
        // channel.basicQos(1);

        // 定义队列的消费者
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [y] Received '" + message + "'");

            //休眠来模拟请求
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // 返回确认状态，注释掉表示使用自动确认模式
            // channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };


        /**
         *
         * 如果没有消息确认机制，消息队列将消息推送给worker后就会从内存中删除了，但是worker还没有将这条消息处理完就挂掉了
         * 那么这条消息就属于丢失了。所以我们希望，当worker处理完消息后给我们返回一个ack表示我已经处理完成了。
         * 如果挂掉了的话，我们就讲消息发送给另一个worker。
         *
         * 什么时候会重发消息：rabbitmq仅在连接断开时才重新发送消息，即使这条消息处理花费的时间非常长。
         *
         * 消息的确认模式分两种
         * 如果这里是true的话，无论消费者获取到消息成功后是否成功消费，都认为是已经成功消费了
         *
         * 手动确认：消费者从队列中获取消息后，服务器会将该消息标记为不可用状态，等待消费者反馈，如果消费者一直没有反馈
         * 那么消息将一直处于不可用
         *
         * 监听队列，false表示手动返回完成状态，true表示自动
         *
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

    }



}
