package work.dduo.ans.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import work.dduo.ans.middleware.impl.RabbitmqServiceImpl;

import java.util.Date;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 注意测试完成后要清空消息队列
 */
@SpringBootTest
public class RabbitMQConnectionTest {

    @Autowired
    private RabbitmqServiceImpl rabbitMqService;

    @Test
    // 测试消息队列的连接
    public void testRabbitMQConnection() throws InterruptedException {
        try {
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    // 进行消息积压测试
    public void testRabbitMQSendAlways() throws InterruptedException {
        try {
            for (int i = 0; i < 1000000000; i++) {
                String testMessage = "Test message at " + new Date();
                rabbitMqService.sendDirect("balloonWords.routingKey", testMessage);
                System.out.println("发送消息成功！"+"第"+i+"次");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    // 仅仅生产消息
    public void testRabbitMQSend() throws InterruptedException {
        try {
            String testMessage = "Test message at " + new Date();
            rabbitMqService.sendDirect("balloonWords.routingKey", testMessage);
            System.out.println("发送消息成功");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    // 仅仅消费消息
    public void testRabbitMQReceive() throws InterruptedException {
        try{
            String message =  rabbitMqService.receiveMessage("balloonWords.queue", 1000);
            System.out.println(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    // 接收指定队列中的下一条消息 带ack确认返回
    public void testRabbitMQReceiveWithAck() throws InterruptedException{
        try{
            String message =  rabbitMqService.receiveMessageWithAck("balloonWords.queue", 1000);
            System.out.println(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    // 查看指定队列的消息数量
    public void getQueueNum(){
        int queueNum = rabbitMqService.getMessageCount("balloonWords.queue");
        System.out.println(queueNum);
    }

}