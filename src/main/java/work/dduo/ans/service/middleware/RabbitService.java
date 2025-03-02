package work.dduo.ans.service.middleware;

import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * RabbitMQ 消息服务封装类
 * 提供消息发送的基础方法封装，支持普通消息、延迟消息、消息确认机制
 */
@Service
public class RabbitService {

    // 注入 RabbitTemplate
    private final RabbitTemplate rabbitTemplate;

    public RabbitService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 发送普通消息
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     */
    public void send(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    /**
     * 发送带消息确认的消息
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     * @param callbackId 自定义回调ID（用于消息确认跟踪）
     */
    public void sendWithConfirm(String exchange, String routingKey, Object message, String callbackId) {
        CorrelationData correlationData = new CorrelationData(callbackId);
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
    }

    /**
     * 发送延迟消息
     * @param exchange    交换机名称
     * @param routingKey  路由键
     * @param message     消息内容
     * @param delayMillis 延迟时间（毫秒）
     */
    public void sendDelayed(String exchange, String routingKey, Object message, int delayMillis) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor -> {
            messagePostProcessor.getMessageProperties().setDelay(delayMillis);
            messagePostProcessor.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return messagePostProcessor;
        });
    }

    /**
     * 发送消息到默认交换机
     * @param routingKey 路由键（队列名称）
     * @param message    消息内容
     */
    public void sendToQueue(String routingKey, Object message) {
        rabbitTemplate.convertAndSend(routingKey, message);
    }

    /**
     * 生成唯一回调ID
     * @return 随机UUID字符串
     */
    public String generateCallbackId() {
        return UUID.randomUUID().toString();
    }
}
