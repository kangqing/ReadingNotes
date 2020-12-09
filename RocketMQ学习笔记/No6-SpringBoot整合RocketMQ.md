# 创建生产者springboot-rocketmq-producer

1.引入依赖
```xml
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
    <version>2.1.1</version>
</dependency>
```


2. 添加配置
```yaml
# nameserver地址
rocketmq.name-server=192.168.16.128:9876;192.168.16.129:9876
# group名称
rocketmq.producer.group=springboot-rocketmq-group
```

3. 详细见同目录下此项目


# 创建消费者springboot-rocketmq-consumer

1. 引入依赖

```xml
<!--添加WEB启动以来，因为消费者要一直运行，添加保证不会停止-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
    <version>2.1.1</version>
</dependency>
```

2. 添加配置
```yaml
# nameserver地址
rocketmq.name-server=192.168.16.128:9876;192.168.16.129:9876
# group名称
rocketmq.producer.group=springboot-rocketmq-group
```

3. 详细见同目录下此项目