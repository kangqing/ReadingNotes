package base.batch;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 批量发送消息大于 4MB 需要分割消息
 * @author yunqing
 * @since 2020/12/8 21:54
 */
public class BatchGt4MBProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //1.创建生产者producer并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.制定nameserver地址，多个地址分号隔开
        producer.setNamesrvAddr("192.168.16.128:9876;192.168.16.129:9876");
        //3.启动生产者
        producer.start();
        List<Message> messageList = new ArrayList<>();
        //4.创建消息对象，指定消息主题Topic,Tag，和消息体
        for (int i = 0; i < 100; i++) {
            Message message = new Message("BatchTopic", "tag1", ("Hello rocketmq批量消息" + i).getBytes());
            messageList.add(message);
        }
        // 对于超过 4MB 的消息必须进行分割
        Listsplitter listsplitter = new Listsplitter(messageList);
        while (listsplitter.hasNext()){
            List<Message> next = listsplitter.next();
            //批量发送
            SendResult result = producer.send(next);
            System.out.println("发送结果 ： " + result);
        }
        // 6. 关闭生产者producer
        producer.shutdown();
    }
}
