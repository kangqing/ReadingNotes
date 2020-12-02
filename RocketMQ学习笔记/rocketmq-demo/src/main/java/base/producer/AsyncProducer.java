package base.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.TimeUnit;

/**
 * 发送异步消息:
 * 异步消息通常用在对响应时间敏感的业务场景，即发送端不能容忍长时间的等待Broker的响应，（也可通过回调函数处理Broker响应）
 * @author yunqing
 * @since 2020/12/2 21:28
 */
public class AsyncProducer {
    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        //1.创建生产者producer并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.制定nameserver地址，多个地址分号隔开
        producer.setNamesrvAddr("192.168.16.128:9876;192.168.16.129:9876");
        //3.启动生产者
        producer.start();
        //4.创建消息对象，指定消息主题Topic,Tag，和消息体
        for (int i = 0; i < 10; i++) {
            Message message = new Message("base", "tag2", ("Hello rocketmq异步消息" + i).getBytes());
            //5.发送消息,异步发送消息不会等待Broker返回结果
            producer.send(message, new SendCallback() {
                /**
                 * 发送成功回调函数
                 * @param sendResult
                 */
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送结果：" + sendResult);
                }

                /**
                 * 发送失败回调函数
                 * @param throwable
                 */
                public void onException(Throwable throwable) {
                    System.out.println("发送异常：" + throwable);
                }
            });


            // 线程睡眠1秒
            TimeUnit.SECONDS.sleep(1);
        }
        // 6. 关闭生产者producer
        producer.shutdown();
    }
}
