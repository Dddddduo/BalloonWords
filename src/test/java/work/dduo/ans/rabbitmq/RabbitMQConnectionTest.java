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

    @Test
    // 测试消息队列的连接
    public void testRabbitMQConnection() throws InterruptedException {
        String testMessage = "Test message at " + new Date();
        // 发送测试消息
        rabbitMqService.sendDirect("balloonWords.routingKey", testMessage);
        // 等待消息处理
        Thread.sleep(1000);
        // 验证消息是否到达队列
        String message = rabbitMqService.receiveMessage("balloonWords.queue", 1000);
        // 判断参数
        assertNotNull(message);
        assertEquals(testMessage,message);
        System.out.println(message);
    }

    @Test
    // 仅仅生产消息
    public void testRabbitMQSend() throws InterruptedException {
        String testMessage = "Test message at " + new Date();
        rabbitMqService.sendDirect("balloonWords.routingKey", testMessage);
    }

    @Test
    // 仅仅消费消息
    public void testRabbitMQReceive() throws InterruptedException {
        String message =  rabbitMqService.receiveMessage("balloonWords.queue", 1000);
        System.out.println(message);
    }

}