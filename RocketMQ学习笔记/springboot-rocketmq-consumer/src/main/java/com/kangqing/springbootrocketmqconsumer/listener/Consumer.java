package com.kangqing.springbootrocketmqconsumer.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消费者监听器
 * consumeMode = ConsumeMode.CONCURRENTLY 广播
 * consumeMode = ConsumeMode.ORDERLY 负载均衡
 * 以上消费模式也能配置在 @RocketMQMessageListener 中
 * @author yunqing
 * @since 2020/12/9 21:21
 */
@RocketMQMessageListener(topic = "SpringBoot-Topic", consumerGroup = "${rocketmq.producer.group}")
@Component
public class Consumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("接收到的文字消息 = " + s);
    }
}
