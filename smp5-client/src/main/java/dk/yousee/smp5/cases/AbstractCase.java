package dk.yousee.smp5.cases;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import dk.yousee.smp5.order.model.ExecuteOrderReply;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.OrderService;
import dk.yousee.smp5.order.model.Response;
import dk.yousee.smp5.order.model.SystemException;

public class AbstractCase {

	private SubscriberModel model;
	private OrderService service;
	private String errorMessage;
	private Response response;
	private Acct acct;
	private ExecuteOrderReply lastOrderReply;

	/**
	 * Constructor with the customer account.
	 *
	 * @param acct
	 *            account to work for
	 * @param service
	 *            the service to use to contact sigma
	 */
	public AbstractCase(Acct acct, OrderService service) {
		this.acct = acct;
		this.service = service;
	}

	/**
	 * Constructor with the customer account. - from other use case
	 *
	 * @param model
	 *            the model coming from another use case
	 * @param service
	 *            the service to use to contact sigma
	 */
	public AbstractCase(SubscriberModel model, OrderService service) {
		this.model = model;
		this.acct = model.getAcct();
		this.service = service;
	}

	/**
	 * When we compose orders it is relevant to keep the same model or getting a
	 * new one. <br/>
	 * When you update the customer in one order and update plan in an new order
	 * then you want a new model. <br/>
	 * When you update the customer (and send no order yet) and then update a
	 * plan and want both update to be in same order - then you want to keep the
	 * model. (saying true)
	 *
	 * @param model
	 *            model to optionally keep
	 * @param keepModel
	 *            true == keep it, false == make a new model from response
	 * @return the model to use
	 */
	protected static SubscriberModel selectModel(SubscriberModel model, boolean keepModel) {
		return keepModel ? model : new SubscriberModel(model.getResponse());
	}

	public OrderService getService() {
		return service;
	}

	protected Response getResponse() {
		return response;
	}

	protected void setResponse(Response response) {
		this.response = response;
	}

	protected void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * The error message that has been generated
	 *
	 * @return error message, null means no error
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	public String errorMessageHandler(String original, String detailError) {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document document = docBuilder.parse(new InputSource(new StringReader(detailError)));
			NodeList nodeList = document.getElementsByTagName("smp:errorCode");
			List<String> codelist = new ArrayList<String>();
			List<String> messagelist = new ArrayList<String>();

			for (int s = 0; s < nodeList.getLength(); s++) {
				Node node = nodeList.item(s);
				String value = node.getFirstChild().getNodeValue();
				codelist.add(value);
			}

			NodeList nodeList2 = document.getElementsByTagName("smp:errorMessage");
			for (int s = 0; s < nodeList2.getLength(); s++) {
				Node node = nodeList2.item(s);
				String value = node.getFirstChild().getNodeValue();
				messagelist.add(value);
			}

			for (int i = 0; i < codelist.size(); i++) {
				original = original + "\n" + "errorcode = " + codelist.get(i) + "  errorMessage = " + messagelist.get(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return original;
	}

	public SubscriberModel getModel() {
		if (model == null) {
			buildSubscriberModel();
		}
		return model;
	}

	public void setModel(SubscriberModel model) {
		this.model = model;
	}

	/**
	 * Load the customer response into SubscriberModel.
	 */
	private void buildSubscriberModel() {
		if (response == null) {
			readSubscriber();
		}
		model = new SubscriberModel(response);
	}

	/**
	 * Get the customer response from SMP.
	 *
	 * @throws SystemException
	 *             when technical error
	 */
	public void readSubscriber() {
		try {
			response = service.readSubscription(acct);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			StringBuilder cause = new StringBuilder();
			if (e.getCause() != null) {
				cause.append(", caused by ").append(e.getCause().getMessage());
			}
			throw new SystemException(String.format("readSubscriber failed, got exception: %s%s", e.getMessage(), cause.toString()));
		}
		buildSubscriberModel();
	}

	/**
	 * Returns the account you have set - in case you have forgotten
	 *
	 * @return account id (9 digits)
	 */
	public Acct getAcct() {
		return acct;
	}

	/**
	 * commit the changes to SMP, the order is the order generated in the model.<br/>
	 * precondition: order must be established by model updates<br/>
	 * postcondition: OrderReply made, orderId returned
	 *
	 * @return order response
	 * @throws BusinessException
	 *             when could not send, see the errorMessage as well
	 */
	public ExecuteOrderReply send() throws BusinessException {
		return send(model.getOrder());
	}
	
	public ExecuteOrderReply sendJMS() throws BusinessException {
		return sendJMS(model.getOrder());
	}

	/**
	 * commit the changes to SMP.<br/>
	 * postcondition: OrderReply made, orderId returned
	 *
	 * @param order2send
	 *            the order to send. (should be taken from the model
	 * @return order response
	 * @throws BusinessException
	 *             when could not send, see the errorMessage as well
	 */
	public ExecuteOrderReply send(Order order2send) throws BusinessException {
		setErrorMessage(null);
		try {
			lastOrderReply = service.maintainPlan(order2send);
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		}
		if (lastOrderReply.getErrorMessage() != null) {
			String detailError = lastOrderReply.getXml().getResponse();
			detailError = errorMessageHandler(lastOrderReply.getErrorMessage(), detailError);
			setErrorMessage(detailError);
			throw new BusinessException("When sendng order, got exception: %s", getErrorMessage());
		}
		return lastOrderReply;
	}
	
	public ExecuteOrderReply sendJMS(Order order2send) throws BusinessException {
		setErrorMessage(null);
		try {
			lastOrderReply = service.maintainPlanJMS(order2send);
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		}
		if (lastOrderReply.getErrorMessage() != null) {
			String detailError = lastOrderReply.getXml().getResponse();
			detailError = errorMessageHandler(lastOrderReply.getErrorMessage(), detailError);
			setErrorMessage(detailError);
			throw new BusinessException("When sendng order, got exception: %s", getErrorMessage());
		}
		return lastOrderReply;
	}

	protected void ensureAcct() throws BusinessException {
		if (!getModel().customerExists()) {
			throw new BusinessException("Operation failed,  Cannot create/update/delete when the customer does not exist. Acct: %s",
					getAcct());
		}
	}

}
