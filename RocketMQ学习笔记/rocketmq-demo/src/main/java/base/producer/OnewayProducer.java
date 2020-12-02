package base.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.TimeUnit;

/**
 * 发送单向消息：
 * 这种方式主要用在不特别关心发送结果的场景，例如日志发送
 * @author yunqing
 * @since 2020/12/2 21:37
 */
public class OnewayProducer {
    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        //1.创建生产者producer并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.制定nameserver地址，多个地址分号隔开
        producer.setNamesrvAddr("192.168.16.128:9876;192.168.16.129:9876");
        //3.启动生产者
        producer.start();
        //4.创建消息对象，指定消息主题Topic,Tag，和消息体
        for (int i = 0; i < 10; i++) {
            Message message = new Message("base", "tag3", ("Hello rocketmq单向消息" + i).getBytes());
            //5.发送单向消息
            producer.sendOneway(message);

            // 线程睡眠1秒
            TimeUnit.SECONDS.sleep(1);
        }
        // 6. 关闭生产者producer
        producer.shutdown();
    }
}
