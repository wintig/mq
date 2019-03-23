package com.wintig.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @Description kafka监听器
 * @Author wintig
 * @Create 2019-03-23 下午2:37
 */
public class ConsumerListener {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = {"test"})
    public void listen(ConsumerRecord<?, ?> record) {
        logger.info("kafka消费成功key为: " + record.key());
        logger.info("kafka消费成功value为:" + record.value().toString());
    }

}
