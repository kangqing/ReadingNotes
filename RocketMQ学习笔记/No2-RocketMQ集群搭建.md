1. 各个角色介绍

    - Producer : 消息的发送者，生产者
    
    - Consumer : 消息的消费者
    
    - Broker : 暂存和传输消息，例如,邮局。
    
    - NameServer : 管理Broker
    
    - Topic : 区分消息的种类；一个发送者可以发送消息给一个或者多个Topic;一个消息的接收者可以订阅一个或者多个Topic 消息
    
    - Message Queue : 相当于是Topic 的分区（子类别）；用于并行发送和接收消息
    
![示例图](https://yunqing-img.oss-cn-beijing.aliyuncs.com/hexo/article/202011/30-rocketmq-01.png)

    如上图所示：生产者询问NameServer后将消息发送给指定 Broker, 多个Broker把自己提交到NameServer管理，消费者通过NameServer知道该去
    哪个Broker消费消息。
    
2. 集群的搭建方式

    - NameServer之间没有什么联系，多个NameServer都会收到Broker上报的信息
    
    - 生产者和消费者也都是多个集群之间没有特别的关系
    
    - 重点在Broker集群，Broker主节点负责写入消息，从节点负责消费者从中读取消息。BrokerName区分一组Broker, 一组中一主多从，Broker Id
      为 0 代表主节点，非 0 代表从节点，一个主节点可以有多个从节点。 Master也可以部署多个。每个Broker与NameServer集群中的所有节点建立
      长连接，定期注册 Topic 信息到所有的 NameServer.
      
    - 生产者与NameServer其中一个节点（随机选择）建立长连接，定期从Nameserver中取Topic路由信息，并向提供 Topic服务的Master建立长连接，
      且定时向Master发送心跳。 Producer完全无状态，可集群部署。
      
    - Consumer消费者与NameServer其中一个节点（随机选择）建立长连接，定期从Nameserver中取Topic路由信息，并向提供 Topic服务的Master、
      Slave建立长连接，且定时向Master、Slave发送心跳。Consumer既可以从主节点订阅消息，也可以从Slave节点订阅消息，订阅规则由Broker配置
      决定。
      
3. 集群模式：

    1).单master模式
    这种方式风险比较大，一旦Broker重启或者宕机时，会导致整个服务不可用。不建议线上环境使用，可以用于本地调试。
    
    2).多master模式
    一个集群无Slave,全是master,例如2个或者3个master这种模式的优缺点如下：
    - 优点：配置简单，单个master宕机或者重启对应用没有影响，在磁盘配置为RAID10时，即使几区宕机不可恢复情况下，由于RAID10磁盘非常可靠，
        消息也不会丢失（移步刷盘丢失少量消息，同步刷盘一条不丢），性能最高。
    - 缺点：单台机器宕机期间，这台机器上未被消费的消息在机器恢复之前不可订阅，消息实时性会受到影响。
    
    3).多master多slave模式（异步）（最常见的模式）
    每个master配置一个Slave,有多对Master-Slave，HA采用异步复制的方式，主备有短暂消息延迟（毫秒级），这种模式的优缺点如下：
    - 优点：即使磁盘损坏，消息丢失的非常少，且消息实时性不会受到影响，同时master宕机后，消费者仍然可以从Slave消费，而且此过程对应用透明，
    不需要人工干预，性能同多master模式几乎一样。
    - 缺点：master宕机，磁盘损坏情况下会丢失少量消息。
    
    4).多master多Slave模式（同步）（最常见的模式）
    每个master配置一个Slave，有多对master-slave,HA采用同步双写方式，即只有主备都写成功，才会向应用返回成功，这种模式的优缺点如下：
    - 优点： 数据与服务都无单点故障，master宕机情况下，消息无延迟，服务可用性与数据可用性能都非常高；
    - 缺点： 性能比异步复制模式略低（大约低10%左右）发送单个消息的RT会略高，且目前版本在主节点宕机后，备机不能自动切换为主机。
    
4. 双主双从集群搭建：
    
    消息高可用采用 2m-2s（2master-2slave）（同步双写）方式
    
集群工作流程介绍：

    1. 启动NameServer, NameServer起来后监听端口，等待Broker、Producer、Consumer连上来，相当于一个路由控制中心。
    
    2. Broker启动，跟所有的NameServer保持长连接，定时发送心跳包。心跳包中包含当前Broker信息（IP+端口等）以及存储所有Topic信息。注册
    成功后，NameServer集群中就有Topic跟Broker的映射关系。
    
    3. 收发消息前，先创建Topic，创建Topic时需要指定该Topic要存储在哪些Broker上，也可以在发送消息时自动创建Topic.
    
    4. Producer发送消息，启动时先跟NameServer集群中的其中一个建立长连接，并从NameServer中获取当前发送的Topic应该存在哪些Broker中，
    轮询从队列列表中选择一个队列，然后与队列所在的Broker建立长连接，从而向Broker发送消息。
    
    5. Consumer跟Producer类似，跟其中一台NameServer建立长连接，获取当前订阅的Topic存在哪些Broker上，然后直接跟Broker建立连接通道，
    开始消费消息。
    
5. 开始搭建：
    
### 准备两个服务器：

   192.168.25.135 中部署nameserver1, broker主1 从2
   192.168.25.138 中部署nameserver2, broker主2 从1
    
### host添加信息

```shell script
vim /etc/hosts

# 添加如下配置

# nameserver
192.168.25.135 rocketmq-nameserver1
192.168.25.138 rocketmq-nameserver2
# broker
192.168.25.135 rocketmq-master1
192.168.25.135 rocketmq-slave2
192.168.25.138 rocketmq-master2
192.168.25.138 rocketmq-slave1
```
    
### 配置完成后重启网卡
```shell script
systemctl restart network
```

### 防火墙设置

学习阶段可以直接关闭防火墙：
```shell script
# 关闭防火墙
systemctl stop filewalld.service
# 查看防火墙状态
firewall-cmd --state
# 禁止开机启动防火墙
systemctl disable firewalld.service
```

如果是上线之后，需要只开放特定的端口号，RocketMQ默认使用三个端口号9876/10911/11011，开启防火墙的话需要开放此三个端口

- nameserver默认使用9876端口
- master默认使用10911端口
- slave默认使用11011端口

```shell script
# 开放单个端口号
firewall-cmd --remove-port=9876/tcp --permanent
# 重启防火墙
firewall-cmd --reload
```

### 环境变量配置（为了不用每次都进入bin目录操作）
```shell script
# 进入配置
vim /etc/profile

# 在文件末尾java的配置下加入，注意改成你自己的rocketmq的路径
# set rocketmq
ROCKETMQ_HOME=/opt/software/rocketmq/rocketmq-all-4.7.1-bin-release
PATH=$PATH:$ROCKETMQ_HOME/bin
export ROCKETMQ_HOME PATH

# :wq保存退出，执行使之生效
source /etc/profile

```

### 创建消息存储路径

```shell script
# 我存在我的rocketmq所在的文件路径下了
mkdir /opt/software/rocketmq/store
mkdir /opt/software/rocketmq/store/commitlog
mkdir /opt/software/rocketmq/store/consumequeue
mkdir /opt/software/rocketmq/store/index
```

**以上两个服务器均进行如上面几步配置**






