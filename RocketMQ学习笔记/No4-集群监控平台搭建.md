RocketMQ有一个对其扩展的开源项目[incubator-rocketmq-externals](https://github.com/apache/rocketmq-externals) ,这个项目有一个子模块叫`rocketmq-console`，这个便是管理控制台项目了，
先将项目incubator-rocketmq-externals拉取到本地，需要自己对`rocketmq-console`进行编译打包运行。

```shell script

git clone https://github.com/apache/rocketmq-externals.git

cd rocketmq-console

# 打包之前修改rocketmq-console中配置namesrv集群的地址：
rocketmq.config.namesrvAddr=192.168.25.135:9876;192.168.25.138:9876

# 打包，跳过测试
mvn clean package -Dmaven.test.skip=true
```

打包成功后把target中的jar包启动
```shell script
java -jar 生成的jar包.jar
```