package dk.yousee.smp.order.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 13, 2010
 * Time: 10:33:01 AM<br/>
 * Response coming from readSubscriber<br/>
 * addSubscriber also uses this VO (because it is a readSubscriber at the end)
 *
 * This is a 1:1 mapping of the response XML document smpcbe:getEntityByKeyResponse<br/>
 * It consists of:
 *       smp : ResponseEntity
 *            entities {of various type}
 *               
 *
 *          
 */
public final class Response implements Serializable {
    static final private long serialVersionUID = -7569650801379990454L;

    /**
     * Constructing a response for serialization
     */
    public Response() {
    }

    public Response(Acct acct, Integer orderId, SmpXml xml, String errorMessage, ResponseEntity smp) {
        this.acct = acct;
        this.orderId = orderId;
        this.xmlRequest = xml.getRequest();
        this.xmlResponse = xml.getResponse();
        this.errorMessage = errorMessage;
        this.smp = smp;
    }
    /**
     * The account it is all about (9 digit customer id)
     */
    private Acct acct;
    /**
     * The account this response was made from
     * @return account - customers 9 digit account number
     */
    public Acct getAcct() {
        return acct;
    }

    public void setAcct(Acct acct) {
        this.acct = acct;
    }

    //set only when we create a new subscriber.
    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }

    /**
     * The request send to sigma - as string
     */
    private String xmlRequest;

    /**
     * @return the xml we send to sigma
     */
    public String getXmlRequest() {
        return xmlRequest;
    }

    /**
     * The response that came back from sigma - as string
     */
    private String xmlResponse;
    /**
     * @return the xml we got back from sigma
     */
    public String getXmlResponse() {
        return xmlResponse;
    }

//    public void setXmlResponse(String xmlResponse) {
//        this.xmlResponse = xmlResponse;
//    }

    /**
     * Assume we got error, then the message is here
     */
	private String errorMessage;

//	public void setErrorMessage(String errorMessage) {
//		this.errorMessage = errorMessage;
//	}

    /**
     * @return error from sigma that was produced under the process of making the response
     */
	public String getErrorMessage() {
		return errorMessage;
	}

    private ResponseEntity smp;

    public ResponseEntity getSmp() {
        return smp;
    }

    public void setSmp(ResponseEntity smp) {
        this.smp = smp;
    }
}
