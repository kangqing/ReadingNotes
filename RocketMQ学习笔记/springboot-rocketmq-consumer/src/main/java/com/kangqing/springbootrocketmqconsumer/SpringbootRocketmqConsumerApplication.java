package com.kangqing.springbootrocketmqconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootRocketmqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRocketmqConsumerApplication.class, args);
        System.out.println("消费者启动成功");
    }

}
