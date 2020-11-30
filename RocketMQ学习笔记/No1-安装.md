1. 下载解压二进制文件

```shell script
wget https://mirrors.tuna.tsinghua.edu.cn/apache/rocketmq/4.7.1/rocketmq-all-4.7.1-bin-release.zip

unzip rocketmq-all-4.7.1-bin-release.zip
```

2. 目录简介：

    benchmark : 一些可以直接运行测试的 demo
    bin : 可执行文件
    conf : 配置文件
    bin : 依赖的第三方 jar 包
    
3. 安装jdk1.8

```shell script
wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-x64.tar.gz

tar -zxvf jdk-8u131-linux-x64.tar.gz

mv jdk1.8.0_131  jdk8

# 打开文件
vi ~/.bashrc

# 在# .bashrc下面 添加下面两行，修改成你自己的解压目录
export JAVA_HOME=/opt/software/jdk1.8/jdk8
export PATH=$PATH:$JAVA_HOME/bin

# 让修改的配置文件生效
source ~/.bashrc

java -version
```

4. 修改默认虚拟机内存，RocketMQ默认的内存太大，改小一点，不然启动Broker时候会内存不足失败，需要修改如下文件中的 `JAVA_OPT=`
```shell script
vim runbroker.sh

vim runsercer.sh
```

5. 启动RocketMQ
```shell script
# 1. 启动 NameServer
nohup sh bin/mqnamesrv &

# 2. 查看启动日志
tail -f ~/logs/rocketmqlogs/namesrv.log

# 3. 启动broker
nohup sh bin/mqbroker -n localhost:9876 &

# 4. 查看启动日志
tail -f ~/logs/rocketmqlogs/broker.log

# 或者直接jps看
jps
# jps获取如下结果代表启动成功
2193 NamesrvStartup
2630 BrokerStartup
2839 Jps
```

6. 测试 RocketMQ
    
    为了方便测试，开两个窗口模拟`生产者`和`消费者`
```shell script
# 1. 生产者设置临时环境变量
export NAMESRV_ADDR=localhost:9876

# 2. 使用安装包的 Demo 发送消息
sh bin/tools.sh org.apache.rocketmq.example.quickstart.Producer

# 3. 在另一个窗口设置消费者临时环境变量
export NAMESRV_ADDR=localhost:9876

# 4. 接收消息
sh bin/tools.sh org.apache.rocketmq.example.quickstart.Consumer

# 5. 可以看到发送窗口发送完就终止了， 而消费者一直在监听中，尝试生产窗口再次发送，监听方还是能消费消息
sh bin/tools.sh org.apache.rocketmq.example.quickstart.Producer
```

7. 关闭 RocketMQ
```shell script
# 1. 关闭NameServer
sh bin/mqshutdown namesrv

# 2. 关闭broker
sh bin/mqshutdown broker
```


    