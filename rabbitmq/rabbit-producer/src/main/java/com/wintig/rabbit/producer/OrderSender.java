package com.wintig.rabbit.producer;


import com.wintig.rabbit.entity.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            String messageID = correlationData.getId();

            if (ack) {
                System.out.println("订单 " + messageID + " 处理成功");
            } else {

                // 重发或者补偿
                System.err.println("订单 " + messageID + " 处理失败");
            }

        }
    };

    public void send(Order order) throws Exception {

        // 消息唯一ID
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getMessageID());

        /**
         * 收到ack后的回调
         */
        rabbitTemplate.setConfirmCallback(confirmCallback);

        rabbitTemplate.convertAndSend("order-exchange",
                "order.abcd",       // 路由key
                order,
                correlationData     //消息唯一id
        );
    }

}
