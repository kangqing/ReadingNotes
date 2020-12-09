package base.transaction;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.TimeUnit;

/**
 * 事务消息生产者
 * @author yx
 * @since 2020/12/9 15:22
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //1.创建事务消息生产者producer并制定生产者组名
        TransactionMQProducer producer = new TransactionMQProducer("group2");
        //2.制定nameserver地址，多个地址分号隔开
        producer.setNamesrvAddr("192.168.16.128:9876;192.168.16.129:9876");
        // 设置消息事务监听器，本地事务的入口
        producer.setTransactionListener(new TransactionListener() {
            /**
             * 在该方法执行本地事务
             * @param message
             * @param o
             * @return
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                if (StringUtils.equals("tagA", message.getTags())) {
                    // 提交
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else if (StringUtils.equals("tagB", message.getTags())) {
                    // 回滚
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                } else if (StringUtils.equals("tagC", message.getTags())) {
                    // 回查
                    return LocalTransactionState.UNKNOW;
                }
                return LocalTransactionState.UNKNOW;
            }

            /**
             * MQ消息事务状态回查
             * @param messageExt
             * @return
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                System.out.println("消息的Tag = " + messageExt.getTags());
                // 提交消息
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });
        String[] tag = {"tagA", "tagB", "tagC"};
        //3.启动生产者
        producer.start();
        //4.创建消息对象，指定消息主题Topic,Tag，和消息体
        for (int i = 0; i < 3; i++) {
            Message message = new Message("TransactionTopic", tag[i], ("Hello rocketmq事务消息" + i).getBytes());
            //5.发送消息,注意事务消息需要使用的发送方法
            // null 代表不针对某一条消息进行事务控制，即全部进行事务控制
            SendResult send = producer.sendMessageInTransaction(message, null);
            System.out.println("消息 = " + send);

            // 线程睡眠1秒
            TimeUnit.SECONDS.sleep(1);
        }
        // 6. 关闭生产者producer,提交完可能回查，这里就不要关闭
        //producer.shutdown();
    }
}
