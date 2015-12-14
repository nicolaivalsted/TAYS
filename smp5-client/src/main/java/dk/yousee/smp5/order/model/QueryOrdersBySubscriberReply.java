package dk.yousee.smp5.order.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp5.order.model.OrderInfo;
import dk.yousee.smp5.order.model.Smp5Xml;

/**
 * @author m64746
 *
 *         Date: 09/11/2015 Time: 17:59:31
 */
public class QueryOrdersBySubscriberReply implements Serializable {

	private static final long serialVersionUID = 9149930559583548037L;
	private List<OrderInfo> orderInfoList;
	private String xmlRequest;
	private String xmlResponse;
	private String errorMessage;

	/**
	 * @deprecated only to be used by remote serialization
	 */
	public QueryOrdersBySubscriberReply() {
	}

	/**
	 * Successful listing of orders
	 * 
	 * @param orderInfoList
	 *            list produced
	 * @param xml
	 *            strings
	 */
	public QueryOrdersBySubscriberReply(List<OrderInfo> orderInfoList, Smp5Xml xml) {
		this.orderInfoList = orderInfoList;
		setXmlLog(xml);
	}

	/**
	 * Failed
	 * 
	 * @param errorMessage
	 *            error
	 * @param xml
	 *            strings
	 */
	public QueryOrdersBySubscriberReply(String errorMessage, Smp5Xml xml) {
		orderInfoList = new ArrayList<OrderInfo>();
		this.errorMessage = errorMessage;
		setXmlLog(xml);
	}

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
		if (getErrorMessage() != null)
			sb.append(", \"errorMessage\":\"").append(errorMessage).append('"');
		sb.append('}');
		return sb.toString();
	}

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

	private void setXmlLog(Smp5Xml xml) {
		this.xmlRequest = xml.getRequest();
		this.xmlResponse = xml.getResponse();
	}

	public Smp5Xml getXml() {
		return new Smp5Xml(xmlRequest, xmlResponse);
	}

}
