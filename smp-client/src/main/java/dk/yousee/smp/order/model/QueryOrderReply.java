package dk.yousee.smp.order.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Mar 31, 2011
 * Time: 10:46:19 AM
 * Query result
 */
public class QueryOrderReply implements Serializable {

    private static final long serialVersionUID = 7250715510040736064L;

    /**
     * @deprecated only for serialization
     */
    public QueryOrderReply() {
    }

    /**
     * Successful processing
     *
     * @param smp        engagement from smp
     * @param orderState state..
     * @param xml        strings
     * @deprecated smp does not make sense here
     */
    public QueryOrderReply(ResponseEntity smp, OrderStateEnum orderState, SmpXml xml) {
        this.orderState = orderState;
        setXml(xml);
    }
    /**
     * Successful processing
     *
     * @param orderState state..
     * @param xml        strings
     */
    public QueryOrderReply(OrderStateEnum orderState, SmpXml xml) {
        this.orderState = orderState;
        setXml(xml);
    }

    /**
     * Failed
     *
     * @param errorMessage error
     * @param xml          strings
     */
    public QueryOrderReply(String errorMessage, SmpXml xml) {
        this.errorMessage = errorMessage;
        setXml(xml);
    }

    private OrderStateEnum orderState;

    public OrderStateEnum getOrderState() {
        return orderState;
    }
    /**
     * The error message we get from SMMP
     */
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * The orderxml we send to SMMP
     */
    private String xmlRequest;
    /**
     * The responsexml we get from SMMP
     */
    private String xmlResponse;

    public SmpXml getXml() {
        return new SmpXml(xmlRequest, xmlResponse);
    }

    private void setXml(SmpXml xml) {
        this.xmlRequest = xml.getRequest();
        this.xmlResponse = xml.getResponse();
    }


    public String getXmlRequest() {
        return xmlRequest;
    }

    public String getXmlResponse() {
        return xmlResponse;
    }
}
