package dk.yousee.smp.functions;

import dk.yousee.smp.com.ReadSubscriptionCom;
import dk.yousee.smp.com.SigmaAction;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.ExecuteOrderReply;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;
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

	/* (non-Javadoc)
	 * @see dk.yousee.smp.order.model.OrderService#searchSubscriber(dk.yousee.smp.order.model.SearchCustomersRequest)
	 */
	@Override
	public SearchCustomersResponse searchSubscriber(SearchCustomersRequest searchCustomersRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dk.yousee.smp.order.model.OrderService#addSubscription(dk.yousee.smp.order.model.Order)
	 */
	@Override
	public Response addSubscription(Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see dk.yousee.smp.order.model.OrderService#maintainPlan(dk.yousee.smp.order.model.Order)
	 */
	@Override
	public ExecuteOrderReply maintainPlan(Order order) {
		// TODO Auto-generated method stub
		return null;
	}

}
