package dk.yousee.smp5.order.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Jun 22, 2011
 * Time: 11:12:11 AM
 * The open date, finish date, order nr, and order state for each orders.
 */
public class OrderInfo implements Serializable {
    private static final long serialVersionUID = 3533007864507921876L;

    Date orderDate;
    Date completionDate;
    OrderStateEnum stateEnum;
    String orderId;
    boolean manualtask;

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public OrderStateEnum getStateEnum() {
        return stateEnum;
    }

    public void setStateEnum(OrderStateEnum stateEnum) {
        this.stateEnum = stateEnum;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isManualtask() {
        return manualtask;
    }

    public void setManualtask(boolean manualtask) {
        this.manualtask = manualtask;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"orderDate\":").append(orderDate);
        if (getCompletionDate() != null) sb.append(", \"completionDate\":").append(completionDate);
        if (stateEnum != null) sb.append(", \"stateEnum\":").append(stateEnum);
        if (orderId != null) sb.append(", \"orderId\":\"").append(orderId).append('"');
        if (isManualtask()) sb.append(", \"manualtask\":").append(manualtask);
        sb.append('}');
        return sb.toString();
    }
}


