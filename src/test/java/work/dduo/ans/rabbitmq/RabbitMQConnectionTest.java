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
    public void testRabbitMQConnection() throws InterruptedException {
        String testMessage = "Test message at " + new Date();
        // 发送测试消息
        rabbitMqService.send("demo.routingKey", testMessage);
        // 等待消息处理
        Thread.sleep(1000);
        // 验证消息是否到达队列
        Message received = rabbitTemplate.receive("demo.queue", 3000);
        assertNotNull(received);
        assertEquals(testMessage, new String(received.getBody()));
        System.out.println(received);
    }

}
