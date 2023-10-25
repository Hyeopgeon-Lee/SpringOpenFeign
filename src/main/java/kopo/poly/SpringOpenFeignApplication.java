package kopo.poly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SpringOpenFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringOpenFeignApplication.class, args);
    }

}
