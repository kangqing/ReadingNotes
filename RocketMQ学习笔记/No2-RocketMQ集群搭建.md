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
      

    