package com.github.dragonhht.kafka;

import org.apache.kafka.clients.producer.*;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 生产者.
 *
 * @author: huang
 * @Date: 2019-6-6
 */
public class ProducerTest {

    @Test
    public void producer() throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "my.dragon.com:9092");
        // 判别请求是否为完整的条件， “all”将会阻塞消息，这种设置性能最低，但是是最可靠的。
        properties.put("acks", "all");
        // 如果请求失败，生产者会自动重试
        properties.put("retries", 0);
        // 缓存大小
        properties.put("batch.size", 16384);
        // 指示生产者发送请求之前等待一段时间
        properties.put("linger.ms", 1);
        // 控制生产者可用的缓存总量
        properties.put("buffer.memory", 33554432);
        // key.serializer和value.serializer 将用户提供的key和value对象ProducerRecord转换成字节
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(properties);
        // 异步发送一条消息到topic，会返回一个RecordMetadata
        producer.send(new ProducerRecord<String, String>("test-topic", "hello", "true"));
        // 调用get()使其阻塞
        producer.send(new ProducerRecord<String, String>("test-topic1", "hello", "world")).get();
        // 使用回调
        producer.send(new ProducerRecord<String, String>("test-topic2", "hello", "dragon"),
                new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e != null)
                            e.printStackTrace();
                        System.out.println("回调： " + recordMetadata.topic());
                    }
                });
        producer.close();
    }

}
