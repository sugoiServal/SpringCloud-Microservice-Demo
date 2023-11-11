import org.example.amqp.AmqpConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AmqpConfig.class)
public class TestMQConsumer {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RabbitAdmin rabbitAdmin;

    @Test
    void testWorkQueue() {
        rabbitAdmin.declareQueue(new Queue("work.queue"));
        String foo = (String) rabbitTemplate.receiveAndConvert("work.queue");
        System.out.println(foo);
    }
}
