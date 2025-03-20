package work.dduo.ans.middleware.impl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.springframework.amqp.core.Message;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;


@Service
public class RabbitmqServiceImpl {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    /**
     * 发送消息到指定交换机（Direct模式）
     * @param routingKey 路由键（匹配绑定到交换机的队列）
     * @param message 要发送的消息内容
     * 示例：sendDirect("order.created", "订单ID:12345")
     */
    public void sendDirect(String routingKey, String message) {
        rabbitTemplate.convertAndSend("balloonWords.exchange", routingKey, message);
    }

    /**
     * 发送延时消息（需要安装rabbitmq_delayed_message_exchange插件）
     * @param exchange 交换机名称
     * @param routingKey 路由键
     * @param message 消息内容
     * @param delayMillis 延迟时间（毫秒）
     */
    public void sendDelayed(String exchange, String routingKey, Object message, int delayMillis) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message, msg -> {
            msg.getMessageProperties().setDelay(delayMillis);
            return msg;
        });
    }

    /**
     * 接收指定队列中的下一条消息
     * @param queueName 队列名称
     * @param timeoutMillis 超时时间（毫秒）
     * @return 接收到的消息，超时返回null
     * 示例：receiveMessage("order.queue", 5000)
     */
    public String receiveMessage(String queueName, long timeoutMillis) {
        Message message = rabbitTemplate.receive(queueName, timeoutMillis);
        return message != null ? new String(message.getBody()) : null;
    }

    /**
     * 接收指定队列中的下一条消息 带ack确认返回
     * @param queueName 队列名称
     * @param timeoutMillis 超时时间（毫秒）
     * @return 接收到的消息，超时返回null
     * 示例：receiveMessage("order.queue", 5000)
     */
    public String receiveMessageWithAck(String queueName, long timeoutMillis) throws Exception {
        Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(false);
        GetResponse response = channel.basicGet(queueName,  false); // 关闭自动ACK
        if (response != null) {
            String body = new String(response.getBody(),  StandardCharsets.UTF_8);
            channel.basicAck(response.getEnvelope().getDeliveryTag(),  false);
            return body;
        }
        return null;
    }

    /**
     * 获取队列当前消息数量
     * @param queueName 队列名称
     * @return 消息总数（需要管理权限）
     */
    public int getMessageCount(String queueName) {
        return rabbitTemplate.execute(channel -> {
            AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queueName);
            return declareOk.getMessageCount();
        });
    }

    /**
     * 发送广播消息（Fanout模式）
     * @param exchange 广播交换机名称
     * @param message 广播消息内容
     */
    public void broadcast(String exchange, Object message) {
        rabbitTemplate.convertAndSend(exchange, "", message);
    }

}
