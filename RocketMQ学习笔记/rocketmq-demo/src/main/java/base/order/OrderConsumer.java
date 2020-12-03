package base.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 顺序消费者
 * @author yunqing
 * @since 2020/12/3 22:07
 */
public class OrderConsumer {
    public static void main(String[] args) throws MQClientException {
        //1.创建消费者consumer，制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        //2.制定Nameserver地址
        consumer.setNamesrvAddr("192.168.16.128:9876;192.168.16.129:9876");
        //3.订阅主题Topic和Tag,这里可以订阅tag1同步消息、tage2异步消息， tag3单向消息， 都消费则可以用 || 连接。
        //例如： tage1 || tag2 || tag3
        consumer.subscribe("orderTopic", "*");
        //4.注册消息监听器(MessageListenerOrderly顺序相关监听器)
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                for (MessageExt messageExt : list) {
                    System.out.println("线程：【" + Thread.currentThread().getName() + "】消费消息：" + new String(messageExt.getBody()));
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        //5.启动消费者
        consumer.start();
        System.out.println("消费者启动了");
    }
}
