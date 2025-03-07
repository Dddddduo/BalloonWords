package work.dduo.ans.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    // 定义队列
    @Bean
    public Queue balloonWordsQueue() {
        return new Queue("balloonWords.queue", true); // 持久化队列
    }

    // 定义直连交换机
    @Bean
    public DirectExchange balloonWordsExchange() {
        return new DirectExchange("balloonWords.exchange");
    }

    // 未绑定队列到交换机
    @Bean
    public Binding demoBinding() {
        return BindingBuilder.bind(balloonWordsQueue())
                .to(balloonWordsExchange())
                .with("balloonWords.routingKey"); // 需与发送时routingKey一致
    }

}
