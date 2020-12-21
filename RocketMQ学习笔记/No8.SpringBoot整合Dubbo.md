## 1.添加依赖
```xml
<!--dubbo-->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.7.8</version>
</dependency>
<!--nacos注册中心-->
<dependency>
    <groupId>com.alibaba.nacos</groupId>
    <artifactId>nacos-client</artifactId>
    <version>1.3.2</version>
</dependency>
<!--zookeeper注册中心-->
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>5.1.0</version>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>5.1.0</version>
</dependency>
```

## 2.配置文件
```yaml
dubbo:
  application:
    name: user-server # 注册服务名
  registry:
    address: zookeeper://127.0.0.1:2181;zookeeper://192.168.16.128:2182
    #address: nacos://127.0.0.1:8848 # 注册中心地址
  protocol:
    name: dubbo # 协议
    port: 20880 # 协议端口
  monitor:
    protocol: registry # 监控中心地址，去注册中心自动发现
```

## 3.开启Dubbo

```java
@EnableDubbo
@SpringBootApplication
public class DubboProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboProducerApplication.class, args);
    }

}
```