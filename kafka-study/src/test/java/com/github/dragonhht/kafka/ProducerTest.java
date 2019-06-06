package com.github.dragonhht.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

import java.util.Properties;

/**
 * 生产者.
 *
 * @author: huang
 * @Date: 2019-6-6
 */
public class ProducerTest {

    @Test
    public void producer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "my.dragon.com:9092");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(properties);
        // 发送数据
        producer.send(new ProducerRecord<String, String>("test-topic", "hello", "true"));
        producer.close();
    }

}
