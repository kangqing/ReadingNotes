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
    
4. 顺序消息


    消息有序值得是可以按照消息的发送顺序来消费消息。RocketMQ可以严格的保证消息有序，可以分为局部有序或者全局有序。
    
    顺序消费的原理解析：在默认的情况下消息发送会采取轮询的方式把消息发送到不同的队列；而消息消费时候从多个MQ上拉取消息，这种情况下不能保证
    消息消费的顺序。但是如果控制发送的顺序消息只一次发送到一个MQ中，消费的时候只从这个MQ上拉取，就保证了消费的顺序。当发送和消费参与的MQ
    只有一个，则是全局有序，如果多个MQ参与，则是分区有序，即相对于每个MQ，消息都是有序的。
    
    下面使用订单进行分区局部有序的实例。一个订单顺序的流程是：创建、付款、推送、完成。订单号相同的消息会被先后发送到同一个队列中，消费时
    同一个orderId获取到的肯定是同一个队列。
    
5. 延迟消息


    比如在电商里，提交了一个订单就可以发送一个延时消息，例如一个小时候检查这个订单的状态，如果还是未支付就取消订单释放库存。
    
6. 批量消息


    批量发送消息能显著提高传递小消息的性能。限制是这些批量消息应该有相同的topic,相同的waitStoreMsgOK,而且不能是延时消息。此外，这一批量
    消息的总大小不能超过 4MB
    如果消息总长度大于 4MB时候需要把消息进行分割。见rocketmq-demo
    
7. 过滤消息

    （1）Tag过滤
        
        其中星号代表消费所有 tag
        consumer.subscribe("BatchTopic", "tag1 || tag2");
        consumer.subscribe("BatchTopic", "*");
    
    （2）SQL过滤
    
        见rocketmq-demo的filter包
        // 给消息添加额外属性
        message.putUserProperty("a", String.valueOf(i));
        // sql 过滤消息，只消费自定义属性 a > 5 的消息
        consumer.subscribe("FilterTopic", MessageSelector.bySql("a > 5"));
        
8. 事务消息：

    见rocketmq-demo的transaction包，
    事务消息的状态：提交、回滚、中间状态
    处于提交状态的可以被消费，处于回滚状态的消息会在队列中删除，中间状态的消息会进行回查，回查中提交或回滚该消息。      
        
        
    

    
