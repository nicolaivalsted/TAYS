package dk.yousee.smp.order.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Jun 22, 2011
 * Time: 3:55:51 PM
 */
public class QueryOrdersBySubscriberReply implements Serializable {
    private static final long serialVersionUID = 5150661489267655481L;

    /**
     * @deprecated only to be used by remote serialization
     */
    public QueryOrdersBySubscriberReply() {
    }

    /**
     * Successful listing of orders
     * @param orderInfoList list produced
     * @param xml strings
     */
    public QueryOrdersBySubscriberReply(List<OrderInfo> orderInfoList, SmpXml xml) {
        this.orderInfoList = orderInfoList;
        setXmlLog(xml);
    }

    /**
     * Failed
     * @param errorMessage error
     * @param xml strings
     */
    public QueryOrdersBySubscriberReply(String errorMessage, SmpXml xml) {
        orderInfoList = new ArrayList<OrderInfo>();
        this.errorMessage=errorMessage;
        setXmlLog(xml);
    }

    private List<OrderInfo> orderInfoList;


    public List<OrderInfo> getOrderInfoList() {
        return orderInfoList;
    }

    @Deprecated
    public void setOrderInfoList(List<OrderInfo> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }

    @Deprecated
    public void addOrderInfo(OrderInfo orderInfo) {
        if (orderInfoList == null) {
            orderInfoList = new ArrayList<OrderInfo>();
        }
        this.orderInfoList.add(orderInfo);
    }

    private void setXmlLog(SmpXml xml) {
        this.xmlRequest = xml.getRequest();
        this.xmlResponse = xml.getResponse();
    }

    public SmpXml getXml() {
        return new SmpXml(xmlRequest,xmlResponse);
    }
    /**
     * The orderxml we send to SMMP
     */
    private String xmlRequest;
    /**
     * The responsexml we get from SMMP
     */
    private String xmlResponse;
    /**
     * The error message we get from SMMP
     */
    private String errorMessage;

    /**
     * @return request
     * @deprecated uset getXml()
     */
    public String getXmlRequest() {
        return xmlRequest;
    }

    @Deprecated
    public void setXmlRequest(String xmlRequest) {
        this.xmlRequest = xmlRequest;
    }

    /**
     * @return response
     * @deprecated uset getXml()
     */
    public String getXmlResponse() {
        return xmlResponse;
    }

    @Deprecated
    public void setXmlResponse(String xmlResponse) {
        this.xmlResponse = xmlResponse;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Deprecated
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"xml\":").append(getXml());
        if (getErrorMessage() != null) sb.append(", \"errorMessage\":\"").append(errorMessage).append('"');
        sb.append('}');
        return sb.toString();
    }
}
