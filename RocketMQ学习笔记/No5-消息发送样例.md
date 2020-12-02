1. 流程分析

- 导入MQ客户端依赖
```xml
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-client</artifactId>
    <version>4.7.1</version>
</dependency>
```

- 消息发送者步骤分析


    1.创建消息生产者producer，并制定生产者组名
    2.指定NameServer地址
    3.启动producer
    4.创建消息对象，指定主题Topic 、 Tag和消息体
    5.发送消息
    6.关闭生产者producer
    
- 消息消费者步骤分析


    1.创建消费者consumer，制定消费者组名
    2.制定Nameserver地址
    3.订阅主题Topic和Tag
    4.设置回调函数，处理消息
    5.启动消费者consumer
    
2. 发送消息

    
    见rocketmq-demo的base.producer中的例子
    1.发送同步消息:这种可靠性同步的发送方式使用比较广泛，比如：重要的消息通知，短信通知。
    2.发送异步消息:异步消息通常用在对响应时间敏感的业务场景，即发送端不能容忍长时间的等待Broker的响应，（也可通过回调函数处理Broker响应）
    3.发送单向消息：这种方式主要用在不特别关心发送结果的场景，例如日志发送
    
3. 消费消息


    见rocketmq-demo的base.consumer中的例子
    1.负载均衡模式：消费者采用负载均衡方式消费消息，多个消费者共同消费队列消息，每个消费者处理的消息不同
    2.广播模式：消费者采用广播模式消费消息，每个消费者消费的消息都是相同的。

    
