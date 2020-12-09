package com.kangqing.springbootrocketmqproducer;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootRocketmqProducerApplicationTests {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    void testSendMessage() {
        rocketMQTemplate.convertAndSend("SpringBoot-Topic", "springboot整合rocketMQ--我是测试消息");
    }

}
