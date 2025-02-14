package work.dduo.ans;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("work.dduo.ans.mapper")
public class AnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnsApplication.class, args);
    }

}
