package dk.yousee.smp5.order.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Mar 31, 2011 Time: 10:46:19 AM
 * Query result
 */
public class QueryOrderReply implements Serializable {

	private static final long serialVersionUID = 7250715510040736064L;
	private String errorMessage;
	private OrderStateEnum orderState;
	private String xmlRequest;
	private String xmlResponse;

	/**
	 * @deprecated only for serialization
	 */
	public QueryOrderReply() {
	}

	/**
	 * Successful processing
	 *
	 * @param smp
	 *            engagement from smp
	 * @param orderState
	 *            state..
	 * @param xml
	 *            strings
	 * @deprecated smp does not make sense here
	 */
	public QueryOrderReply(ResponseEntity smp, OrderStateEnum orderState, Smp5Xml xml) {
		// this.smp = smp;
		this.orderState = orderState;
		setXml(xml);
	}

	/**
	 * Successful processing
	 *
	 * @param orderState
	 *            state..
	 * @param xml
	 *            strings
	 */
	public QueryOrderReply(OrderStateEnum orderState, Smp5Xml xml) {
		this.orderState = orderState;
		setXml(xml);
	}

	/**
	 * Failed
	 *
	 * @param errorMessage
	 *            error
	 * @param xml
	 *            strings
	 */
	public QueryOrderReply(String errorMessage, Smp5Xml xml) {
		this.errorMessage = errorMessage;
		setXml(xml);
	}

	public OrderStateEnum getOrderState() {
		return orderState;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Smp5Xml getXml() {
		return new Smp5Xml(xmlRequest, xmlResponse);
	}

	private void setXml(Smp5Xml xml) {
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
