package work.dduo.ans.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    // 定义队列
    @Bean
    public Queue demoQueue() {
        return new Queue("demo.queue", true); // 持久化队列
    }

    // 定义直连交换机
    @Bean
    public DirectExchange demoExchange() {
        return new DirectExchange("demo.exchange");
    }
}
