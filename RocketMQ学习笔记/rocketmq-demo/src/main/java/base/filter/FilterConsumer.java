package base.filter;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;


/**
 * @author yunqing
 * @since 2020/12/8 22:23
 */
public class FilterConsumer {
    public static void main(String[] args) throws MQClientException {
        //1.创建消费者consumer，制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        //2.制定Nameserver地址
        consumer.setNamesrvAddr("192.168.16.128:9876;192.168.16.129:9876");
        //3.订阅主题Topic和Tag,这里可以订阅tag1同步消息、tage2异步消息， tag3单向消息， 都消费则可以用 || 连接。
        //例如： tage1 || tag2 || tag3
        // sql 过滤消息，只消费自定义属性 a > 5 的消息
        consumer.subscribe("FilterTopic", MessageSelector.bySql("a > 5"));

        // 设置消费模式： 负载均衡 、 广播模式,
        // 默认的消费模式是负载均衡，可以启动3个消费者进行测试
        //consumer.setMessageModel(MessageModel.BROADCASTING);//改成广播模式
        //4.设置回调函数，处理消息
        // 接收消息的内容
        consumer.registerMessageListener((MessageListenerConcurrently) (messageExts, context) -> {
            for (MessageExt messageExt : messageExts) {
                System.out.println(new String(messageExt.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        //5.启动消费者consumer
        consumer.start();
        System.out.println("消费者启动成功");
    }
}
