import org.example.amqp.AmqpConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(classes = AmqpConfig.class)
public class TestMQProducer {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RabbitAdmin rabbitAdmin;

    @Test
    void testWorkerQueue() throws InterruptedException{
        rabbitAdmin.declareQueue(new Queue("work.queue"));
        String queueName = "work.queue";
        for (int i = 1; i < 50; i++) {
            String message = String.valueOf(i) + ": hello, rabbit";
            rabbitTemplate.convertAndSend(queueName, message);
            Thread.sleep(30);
        }
    }

    @Test
    void testFanoutExchange () throws InterruptedException{
        String exchangeName = "exchange.fanout";
        for (int i = 1; i < 10; i++) {
            String message = String.valueOf(i) + ": hello, every rabbit";
            rabbitTemplate.convertAndSend(exchangeName, "", message);
            Thread.sleep(30);
        }
    }

    @Test
    void testDirectExchange () throws InterruptedException{
        String exchangeName = "exchange.direct";
        List<String> directKey = Arrays.asList("yellow", "red", "blue");
        for (String key : directKey) {
            String message = "hello, " + key + " rabbit!";
            rabbitTemplate.convertAndSend(exchangeName, key, message);
            Thread.sleep(30);
        }
    }

    @Test
    void testTopicExchange () throws InterruptedException{
        String exchangeName = "exchange.topic";
        String news = "A doctor inside Gaza's largest hospital says hundreds of patients are stranded, most operations suspended";
        String weather = "Sunny. Becoming a mix of sun and cloud in the morning. ";
        rabbitTemplate.convertAndSend(exchangeName, "ottawa.news", news);
        rabbitTemplate.convertAndSend(exchangeName, "ottawa.weather", weather);
        Thread.sleep(30);
    }
}
