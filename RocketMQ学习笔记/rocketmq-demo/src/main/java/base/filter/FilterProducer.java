package base.filter;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yunqing
 * @since 2020/12/8 22:19
 */
public class FilterProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //1.创建生产者producer并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.制定nameserver地址，多个地址分号隔开
        producer.setNamesrvAddr("192.168.16.128:9876;192.168.16.129:9876");
        //3.启动生产者
        producer.start();
        //4.创建消息对象，指定消息主题Topic,Tag，和消息体
        for (int i = 0; i < 10; i++) {
            Message message = new Message("FilterTopic", "tag1", ("Hello rocketmq之SQL过滤消息" + i).getBytes());
            // 给消息添加额外属性
            message.putUserProperty("a", String.valueOf(i));
            //5.发送消息,同步发送消息会等待Broker返回结果
            SendResult send = producer.send(message);
            System.out.println("发送结果 = " + send);

            // 线程睡眠1秒
            TimeUnit.SECONDS.sleep(1);
        }
        // 6. 关闭生产者producer
        producer.shutdown();
    }
}
