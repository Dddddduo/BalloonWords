package work.dduo.ans;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("work.dduo.ans.mapper")
@EnableScheduling
public class AnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnsApplication.class, args);
    }

}
