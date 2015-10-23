package dk.yousee.smp5.order.model;

import java.io.Serializable;

import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.ResponseEntity;

public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5529097879587821916L;

	/**
	 * The account it is all about (9 digit customer id)
	 */
	private Acct acct;
	// set only when we create a new subscriber.
	private Integer orderId;
	private ResponseEntity smp;
	/**
	 * Assume we got error, then the message is here
	 */
	private String errorMessage;
	/**
	 * The response that came back from sigma - as string
	 */
	private String xmlResponse;
	/**
	 * The request send to sigma - as string
	 */
	private String xmlRequest;

	public Response(Acct acct, Integer orderId, Smp5Xml xml,
			String errorMessage, ResponseEntity smp) {
		this.acct = acct;
		this.orderId = orderId;
		this.xmlRequest = xml.getRequest();
		this.xmlResponse = xml.getResponse();
		this.errorMessage = errorMessage;
		this.smp = smp;
	}

	/**
	 * The account this response was made from
	 * 
	 * @return account - customers 9 digit account number
	 */
	public Acct getAcct() {
		return acct;
	}

	public void setAcct(Acct acct) {
		this.acct = acct;
	}

	public Integer getOrderId() {
		return orderId;
	}

	/**
	 * @return the xml we send to sigma
	 */
	public String getXmlRequest() {
		return xmlRequest;
	}

	/**
	 * @return the xml we got back from sigma
	 */
	public String getXmlResponse() {
		return xmlResponse;
	}

	/**
	 * @return error from sigma that was produced under the process of making
	 *         the response
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	public ResponseEntity getSmp() {
		return smp;
	}

	public void setSmp(ResponseEntity smp) {
		this.smp = smp;
	}

}
