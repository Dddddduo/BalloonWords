package work.dduo.ans.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import work.dduo.ans.service.middleware.RabbitMqService;

import java.util.Date;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RabbitMQConnectionTest {

    @Autowired
    private RabbitMqService rabbitMqService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    // 测试消息队列的连接
    public void testRabbitMQConnection() throws InterruptedException {
        String testMessage = "Test message at " + new Date();
        // 发送测试消息
        rabbitMqService.send("balloonWords.routingKey", testMessage);
        // 等待消息处理
        Thread.sleep(1000);
        // 验证消息是否到达队列
        Message received = rabbitTemplate.receive("balloonWords.queue", 3000);
        // 判断参数
        assertNotNull(received);
        assertEquals(testMessage, new String(received.getBody()));
        System.out.println(received);
    }

    @Test
    // 仅仅生产消息
    public void testRabbitMQSend() throws InterruptedException {
        String testMessage = "Test message at " + new Date();
        rabbitMqService.send("balloonWords.routingKey", testMessage);
    }

    @Test
    // 仅仅消费消息
    public void testRabbitMQReceive() throws InterruptedException {
        Message received = rabbitTemplate.receive("balloonWords.queue");
        System.out.println(received);
    }

}