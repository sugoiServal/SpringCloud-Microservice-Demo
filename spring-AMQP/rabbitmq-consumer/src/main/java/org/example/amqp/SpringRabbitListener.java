package org.example.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SpringRabbitListener {
    // # Work queue
    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue(String msg) throws InterruptedException{
        System.out.println("Consumer 1 received msg: [" + msg + "] " + LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String msg) throws InterruptedException{
        System.err.println("Consumer 2 received msg: [" + msg + "] "+ LocalTime.now());
        Thread.sleep(200);
    }

    // # Fanout Exchange
    @RabbitListener(queues = "queue1.fanout")
    public void listenFanoutQueue1(String msg) throws InterruptedException{
        System.out.println("Consumer received msg from queue1.fanout: [" + msg + "] " + LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "queue2.fanout")
    public void listenFanoutQueue2(String msg) throws InterruptedException{
        System.err.println("Consumer received msg from queue2.fanout: [" + msg + "] "+ LocalTime.now());
        Thread.sleep(20);
    }

    // # Direct Exchange
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "queue1.direct"),
            exchange = @Exchange(name = "exchange.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void listenDirectQueue1(String msg) throws InterruptedException{
        System.out.println("Consumer received msg from queue1.direct: [" + msg + "] "+ LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "queue2.direct"),
            exchange = @Exchange(name = "exchange.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listenDirectQueue2(String msg) throws InterruptedException{
        System.err.println("Consumer received msg from queue2.direct: [" + msg + "] "+ LocalTime.now());
        Thread.sleep(20);
    }

    // Topic Exchange
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "ottawa.topic"),
            exchange = @Exchange(name = "exchange.topic", type = ExchangeTypes.TOPIC),
            key = "ottawa.#"
    ))
    public void listenTopicQueue1(String msg) throws InterruptedException{
        System.err.println("Consumer received msg from ottawa.topic: [" + msg + "] "+ LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "news.topic"),
            exchange = @Exchange(name = "exchange.topic", type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void listenTopicQueue2(String msg) throws InterruptedException{
        System.err.println("Consumer received msg from news.topic: [" + msg + "] "+ LocalTime.now());
        Thread.sleep(20);
    }
}
