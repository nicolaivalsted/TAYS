package dk.yousee.smp5.order.model;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Mar 4, 2011 Time: 11:54:37 AM
 * the result to search customers. Contains a list of CustomerInfo's
 */
public class SearchCustomersResponse {

	public SearchCustomersResponse(ArrayList<CustomerInfo> customersList, Smp5Xml xml) {

		for (CustomerInfo one : customersList) {
			if (!this.customersList.contains(one)) {
				this.customersList.add(one);
			}
		}
		setXml(xml);
	}

	public SearchCustomersResponse(String errorMessage, Smp5Xml xml) {
		this.errorMessage = errorMessage;
		setXml(xml);
	}

	private ArrayList<CustomerInfo> customersList = new ArrayList<CustomerInfo>();

	private String errorMessage;

	private String xmlRequest;
	private String xmlResponse;

	public ArrayList<CustomerInfo> getCustomersList() {
		return customersList;
	}

	private void setXml(Smp5Xml xml) {
		this.xmlRequest = xml.getRequest();
		this.xmlResponse = xml.getResponse();
	}

	public Smp5Xml getXml() {
		return new Smp5Xml(xmlRequest, xmlResponse);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{\"xml\":").append(customersList);
		if (getErrorMessage() != null)
			sb.append(", \"errorMessage\":\"").append(errorMessage).append('"');
		if (getCustomersList() != null)
			sb.append(", \"list\":").append(customersList);
		sb.append('}');
		return sb.toString();
	}
}
