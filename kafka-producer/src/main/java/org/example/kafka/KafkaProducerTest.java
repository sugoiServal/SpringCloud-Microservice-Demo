package org.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.Future;

@Slf4j
public class KafkaProducerTest {
    public static void main(String[] args) {
        Properties props = new Properties();

        // create connection config for kafka producer
        props.put("bootstrap.servers", "localhost:9094");
//        localhost:9094,
//                ,localhost:51062
        // use batching: controls the amount of time a producer will wait before sending a batch of messages to a Kafka broker
        props.put("linger.ms", 1);

        // controls the acknowledgment behavior:
        // what level of acknowledgments the producer requires from the broker after sending a message
        // before considering the message as "sent" or "committed."
        // acks=0: does not require any acknowledgment from the broker
        // acks=1: require acknowledgment from the broker
        // acks=all: requires acknowledgment from all in-sync replicas (ISR) of the partition.
        props.put("acks", "1");

        // set serializer to key and value: other options: ProtoBuf, Avro...
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        // 1. use send without callback: Blocking
        // Future<RecordMetadata> send(ProducerRecord<K, V> var1);
        while(true) {
            for (int i = 0; i < 100; i++) {
                ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("test",  i%3, null, Integer.toString(i));
                Future<RecordMetadata> future = producer.send(producerRecord);
                try {
                    future.get();
                    System.out.println("successfully written the " + i + "th message.");
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // use send with callback: Async, non-blocking
        // Future<RecordMetadata> send(ProducerRecord<K, V> var1, Callback var2);
//        for (int i = 0; i < 100; i++) {
//            ProducerRecord<String,String> producerRecord = new ProducerRecord<String, String>("test", Integer.toString(i), Integer.toString(i));
//            producer.send(producerRecord, new Callback() {
//                @Override
//                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                    // check if there are Exception and handle Exception
//                    if (e == null) {
//                        // successful
//                        String topic = recordMetadata.topic();
//                        int partition = recordMetadata.partition();
//                        long offset = recordMetadata.offset();
//                        System.out.printf("topic = %s, partition = %d, offset = %d\n", topic, partition, offset);
//                    } else {
//                        // unsuccessful
//                        System.out.println("Error during message production");
//                        log.error(e.getMessage());
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }

//        producer.close();
    }
}