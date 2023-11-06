package org.example.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
public class KafkaConsumerTest {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9094");

        // consumer group: several consumers in the same group can consume to a topic together: this java process belongs to group test
        props.setProperty("group.id", "test");

        // automatically commit offset
        props.setProperty("enable.auto.commit", "true");

        // interval of automatically commit offset
        props.setProperty("auto.commit.interval.ms", "1000");

        // deserializer
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // a consumer can subscribe to a collection of topics
        consumer.subscribe(Arrays.asList("test"));

        // use a loop to pull record from topic constantly
        while (true) {
            // poll a batch of records
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));  // poll(timeout duration)
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }
}

