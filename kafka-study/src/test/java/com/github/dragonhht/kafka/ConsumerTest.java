package com.github.dragonhht.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;

/**
 * 消费者.
 *
 * @author: huang
 * @Date: 2019-6-10
 */
public class ConsumerTest {

    // 自动提交偏移量
    @Test
    public void consumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "my.dragon.com:9092");
        // 消费组名称
        props.put("group.id", "test-consumer");
        // 设置自动提交
        props.put("enable.auto.commit", "true");
        // 控制自动提交频率
        props.put("auto.commit.interval.ms", "1000");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // 订阅指定的Topic，此处为test-topic 和 test-topic2
        consumer.subscribe(Arrays.asList("test-topic", "test-topic2"));
        // 获取消息
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }

}
