package base.delay;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.TimeUnit;

/**
 * 发送延迟消息
 * @author yx
 * @since 2020/12/8 15:53
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //1.创建生产者producer并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.制定nameserver地址，多个地址分号隔开
        producer.setNamesrvAddr("192.168.16.128:9876;192.168.16.129:9876");
        //3.启动生产者
        producer.start();
        //4.创建消息对象，指定消息主题Topic,Tag，和消息体
        for (int i = 0; i < 10; i++) {
            Message message = new Message("DelayTopic", "tag1", ("Hello rocketmq延迟消息" + i).getBytes());

            // 设置延迟时间
            /**
             * 注意：现在RocketMQ只支持特定时间的延迟等级，从1s到2h之间一共分为1-18个等级，直接写1-18的任意数字则对应时间等级
             * 1s  5s  10s  30s  1m  2m  3m  4m  5m  6m  7m  8m  9m  10m  20m  30m  1h  2h
             * 1   2   3    4    5   6   7   8   9   10  11  12  13  14   15   16   17  18
             */
            message.setDelayTimeLevel(2); // 延迟5秒
            //5.发送消息,延迟发送消息会等待Broker返回结果
            SendResult send = producer.send(message);
            // 发送状态
            SendStatus sendStatus = send.getSendStatus();
            // 消息id
            String msgId = send.getMsgId();
            // 消息接收队列id
            int queueId = send.getMessageQueue().getQueueId();
            System.out.println("状态" + sendStatus + "消息id" + msgId + "消息接收队列id" + queueId);

            // 线程睡眠1秒
            TimeUnit.SECONDS.sleep(1);
        }
        // 6. 关闭生产者producer
        producer.shutdown();
    }
}
