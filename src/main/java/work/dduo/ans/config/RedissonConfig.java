package work.dduo.ans.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 单机模式 
        config.useSingleServer().setAddress("redis://113.44.34.176:6379").setPassword("1234");
        return Redisson.create(config);
    }

} 