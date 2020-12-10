## 因为只有一台服务器，所以我只在一台服务器上的不同节点搭建伪集群

1. 安装JDK1.8

2. 下载zookeeper压缩包到服务器

```shell script
wget https://mirror.bit.edu.cn/apache/zookeeper/zookeeper-3.6.2/apache-zookeeper-3.6.2-bin.tar.gz
# 解压
tar -zxvf apache-zookeeper-3.6.2-bin.tar.gz
```
3. 创建 data 目录，将 conf 下的 zoo_sample.cfg 文件改名为 zoo.cfg
```shell script
# 重命名
mv zoo_sample.cfg zoo.cfg

```

4. 建立三个文件夹，模拟三个zookeeper节点
```shell script
# 我在目录下 /opt/software/zookeeper 分别创建三个节点文件夹
mkdir zookeeper-1
mkdir zookeeper-2
mkdir zookeeper-3

# 将当前目录下解压好的 apache-zookeeper-3.6.2-bin 分别复制到三个文件夹中
# 复制  文件 apache..  到指定目录
cp -rf apache-zookeeper-3.6.2-bin/ zookeeper-1
cp -rf apache-zookeeper-3.6.2-bin/ zookeeper-2
cp -rf apache-zookeeper-3.6.2-bin/ zookeeper-3
```

5. 修改默认端口，分别对应 2181  2182  2183,分别在zookeeper-1/2/3中创建数据文件夹data

```shell script
cd zookeeper-1
mkdir data

cd zookeeper-2
mkdir data

cd zookeeper-3
mkdir data

# 修改端口号
vim /opt/software/zookeeper/zookeeper-1/apache-zookeeper-3.6.2-bin/conf/zoo.cfg
dataDir=/opt/software/zookeeper/zookeeper-1/data
clientPort=2181

vim /opt/software/zookeeper/zookeeper-2/apache-zookeeper-3.6.2-bin/conf/zoo.cfg
dataDir=/opt/software/zookeeper/zookeeper-2/data
clientPort=2182

vim /opt/software/zookeeper/zookeeper-3/apache-zookeeper-3.6.2-bin/conf/zoo.cfg
dataDir=/opt/software/zookeeper/zookeeper-3/data
clientPort=2183
```

### 配置集群

1. 在每个zookeeper的data目录下创建一个 myid 文件， 内容分别是 1   2   3  这个文件就是记录每个服务器的 ID
```shell script
# 创建 myid 文件
vim zookeeper-1/data/myid
# 内容为 1
# 退出保存
:wq

# 创建 myid 文件
vim zookeeper-2/data/myid
# 内容为 2
# 退出保存
:wq

# 创建 myid 文件
vim zookeeper-3/data/myid
# 内容为 3
# 退出保存
:wq

```
2. 在每一个zookeeper的zoo.cfg中配置客户端访问端口和集群服务器 IP 列表，集群服务器IP列表如下：

```shell script
# 加到每一个节点的 zoo.cfg文件的最后
# 解释：server.服务器ID = 服务器ip地址：服务器之间通信的端口号 ： 服务器之间投票选举的端口号
server.1=192.168.16.128:2881:3881
server.2=192.168.16.128:2882:3882
server.3=192.168.16.128:2883:3883

```

### 启动集群
