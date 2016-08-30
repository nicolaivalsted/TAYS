package dk.yousee.smp.functions;

import dk.yousee.smp.com.AddSubscriberCom;
import dk.yousee.smp.com.FindOrderCom;
import dk.yousee.smp.com.FindOrdersBySubscriberCom;
import dk.yousee.smp.com.FindSubscriberCom;
import dk.yousee.smp.com.ProvisioningCom;
import dk.yousee.smp.com.ReadSubscriptionCom;
import dk.yousee.smp.com.SigmaAction;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.ExecuteOrderReply;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;
import dk.yousee.smp.order.model.QueryOrderReply;
import dk.yousee.smp.order.model.QueryOrdersBySubscriberReply;
import dk.yousee.smp.order.model.Response;
import dk.yousee.smp.order.model.SearchCustomersRequest;
import dk.yousee.smp.order.model.SearchCustomersResponse;
import dk.yousee.smp.smpclient.SmpConnectorImpl;

/**
 * User: m26778 Date: 20-11-2009 Time: 07:40:52
 */
public class OrderServiceImpl implements OrderService {
	private SmpConnectorImpl connector;

	public void setConnector(SmpConnectorImpl connector) {
		this.connector = connector;
	}

	public String getSigmaServiceUrl() {
		return connector.getUrl();
	}

	public Integer getOperationTimeout() {
		return connector.getOperationTimeout();
	}

	public SmpConnectorImpl getConnector() {
		return connector;
	}

	/**
	 * Take down services, called by Spring at deactivation
	 */
	public void destroy() {
		if (connector != null) {
			connector.destroy();
		}
	}

	private SigmaAction con;

	SigmaAction getCon() {
		if (con == null)
			con = new SigmaAction(getConnector());
		return con;
	}

	public String connectInfo() {
		return getConnector().connectInfo();
	}

	public Response readSubscription(Acct acct) {
		ReadSubscriptionCom com = new ReadSubscriptionCom();
		com.setCon(getCon());
		return com.process(acct);
	}

	public SearchCustomersResponse searchSubscriber(SearchCustomersRequest searchCustomersRequest) {
		FindSubscriberCom com = new FindSubscriberCom();
		com.setCon(getCon());
		return com.process(searchCustomersRequest);
	}

	/**
	 * Find orders by subscriber , return all the orders.
	 */
	public QueryOrdersBySubscriberReply queryOrdersBySubscriber(String subscriberID) {
		FindOrdersBySubscriberCom com = new FindOrdersBySubscriberCom(false);
		com.setCon(getCon());
		return com.process(new Acct(subscriberID));
	}

	/**
	 * Find orders by subscriber ,only return the open orders.
	 */
	public QueryOrdersBySubscriberReply queryOpenOrdersBySubscriber(String subscriberID) {
		FindOrdersBySubscriberCom com = new FindOrdersBySubscriberCom(true);
		com.setCon(getCon());
		return com.process(new Acct(subscriberID));
	}

	public QueryOrderReply queryOrderState(String orderID) {
		FindOrderCom com = new FindOrderCom();
		com.setCon(getCon());
		return com.process(orderID);
	}

	/**
	 * Establish a new subscriber in SMP
	 *
	 * @param order
	 *            to create the subscriber
	 * @return Response as the sum of create and lookup (request/response/order
	 *         from create, data from lookup) Let it only add, and make read
	 *         from the cases !!!!
	 */
	public Response addSubscription(Order order) {

		AddSubscriberCom com = new AddSubscriberCom();
		com.setCon(getCon());
		ExecuteOrderReply reply = com.process(order);

		Acct acct = order.getSubscriber().getKundeId();
		if (reply.getErrorMessage() != null) {
			String detailError = AddSubscriberCom.makePrettyErrorMessage(reply.getErrorMessage(), reply.getXml().getResponse());
			Response response;
			response = new Response(acct, reply.getOrderId(), reply.getXml(), detailError, null);
			return response;
		} else {
			Response response = readSubscription(acct);
			Response sum;
			sum = new Response(acct, reply.getOrderId(), reply.getXml(), response.getErrorMessage(), response.getSmp());
			return sum;
		}

	}

	public ExecuteOrderReply maintainPlan(Order order) {

		ProvisioningCom com = new ProvisioningCom();
		com.setCon(getCon());
		return com.process(order);

	}

}
