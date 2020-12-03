package base.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @author yunqing
 * @since 2020/12/2 21:48
 */
public class Consumer {
    public static void main(String[] args) throws MQClientException {
        //1.创建消费者consumer，制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        //2.制定Nameserver地址
        consumer.setNamesrvAddr("192.168.16.128:9876;192.168.16.129:9876");
        //3.订阅主题Topic和Tag,这里可以订阅tag1同步消息、tage2异步消息， tag3单向消息， 都消费则可以用 || 连接。
        //例如： tage1 || tag2 || tag3
        consumer.subscribe("base", "tag1");

        // 设置消费模式： 负载均衡 、 广播模式,
        // 默认的消费模式是负载均衡，可以启动3个消费者进行测试
        consumer.setMessageModel(MessageModel.BROADCASTING);//改成广播模式
        //4.设置回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            // 接收消息的内容
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt messageExt : messageExts) {
                    System.out.println(new String(messageExt.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //5.启动消费者consumer
        consumer.start();
    }
}
