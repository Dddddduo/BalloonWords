package work.dduo.ans.service.middleware;

import com.rabbitmq.client.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 发送到指定路由键
    public void send(String routingKey, String message) {
        rabbitTemplate.convertAndSend("demo.exchange", routingKey, message);
    }

}
