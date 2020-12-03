package base.order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yunqing
 * @since 2020/12/3 21:23
 */
public class OrderStep {

    private long orderId;

    private String desc;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "OrderStep{" +
                "orderId=" + orderId +
                ", desc='" + desc + '\'' +
                '}';
    }


    public static List<OrderStep> buildOrders() {
        List<OrderStep> orderList = new ArrayList<OrderStep>();

        OrderStep orderStep = new OrderStep();

        orderStep.setOrderId(1039L);
        orderStep.setDesc("创建");
        orderList.add(orderStep);

        orderStep = new OrderStep();
        orderStep.setOrderId(1065L);
        orderStep.setDesc("创建");
        orderList.add(orderStep);

        orderStep = new OrderStep();
        orderStep.setOrderId(1039L);
        orderStep.setDesc("付款");
        orderList.add(orderStep);

        orderStep = new OrderStep();
        orderStep.setOrderId(7235L);
        orderStep.setDesc("创建");
        orderList.add(orderStep);

        orderStep = new OrderStep();
        orderStep.setOrderId(1065L);
        orderStep.setDesc("付款");
        orderList.add(orderStep);

        orderStep = new OrderStep();
        orderStep.setOrderId(7235L);
        orderStep.setDesc("付款");
        orderList.add(orderStep);

        orderStep = new OrderStep();
        orderStep.setOrderId(1065L);
        orderStep.setDesc("完成");
        orderList.add(orderStep);

        orderStep = new OrderStep();
        orderStep.setOrderId(1039L);
        orderStep.setDesc("推送");
        orderList.add(orderStep);

        orderStep = new OrderStep();
        orderStep.setOrderId(7235L);
        orderStep.setDesc("完成");
        orderList.add(orderStep);

        orderStep = new OrderStep();
        orderStep.setOrderId(1039L);
        orderStep.setDesc("完成");
        orderList.add(orderStep);

        return orderList;
    }
}
