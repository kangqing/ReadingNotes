package base.order;

import base.order.OrderStep;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * 顺序发送消息
 * @author yunqing
 * @since 2020/12/3 21:35
 */
public class OrderProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //1.创建生产者producer并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.制定nameserver地址，多个地址分号隔开
        producer.setNamesrvAddr("192.168.16.128:9876;192.168.16.129:9876");
        //3.启动生产者
        producer.start();
        //4.构建消息集合
        List<OrderStep> orderSteps = OrderStep.buildOrders();
        //5. 发送消息
        for (int i = 0; i < orderSteps.size(); i++) {
            String body = orderSteps.get(i) + "";
            Message message = new Message("orderTopic", "orderTag", "i" + i, body.getBytes());
            // 参数1：消息
            // 参数2：匿名类消息队列选择器
            // 参数3：选择器选择队列的依据（这里使用orderId订单号）
            SendResult send = producer.send(message, new MessageQueueSelector() {
                /**
                 * @param list MQ集合
                 * @param message 消息对象
                 * @param o 业务标识参数，即orderId
                 * @return
                 */
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    long orderId = Long.parseLong(String.valueOf(o));
                    long index = orderId % list.size();
                    return list.get((int) index);
                }
            }, orderSteps.get(i).getOrderId());

            System.out.println("发送结果：" + send.getMessageQueue().getQueueId());

        }
        producer.shutdown();

    }
}
