package dk.yousee.smp5.functions;

import dk.yousee.smp5.com.AddSubscriberCom;
import dk.yousee.smp5.com.ProvisioningCom;
import dk.yousee.smp5.com.SigmaAction;
import dk.yousee.smp5.com.ReadSubscriptionCom;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.ExecuteOrderReply;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;
import dk.yousee.smp5.order.model.Response;
import dk.yousee.smp5.smp5client.Smp5ConnectorImpl;

public class OrderServiceImpl implements OrderService {

	private Smp5ConnectorImpl connector;
	private SigmaAction con;

	@Override
	public String getSigmaServiceUrl() {
		return connector.getUrl();
	}

	@Override
	public Integer getOperationTimeout() {
		return connector.getOperationTimeout();
	}

	public String connectInfo() {
		return getConnector().connectInfo();
	}

	public Smp5ConnectorImpl getConnector() {
		return connector;
	}

	public void setConnector(Smp5ConnectorImpl connector) {
		this.connector = connector;
	}

	public void destroy() {
		if (connector != null) {
			connector.destroy();
		}
	}

	@Override
	public Response readSubscription(Acct acct) {
		ReadSubscriptionCom com = new ReadSubscriptionCom();
		com.setCon(getCon());
		return com.process(acct);
	}

	SigmaAction getCon() {
		if (con == null)
			con = new SigmaAction(getConnector());
		return con;
	}

	@Override
	public Response addSubscription(Order order) {
		AddSubscriberCom com = new AddSubscriberCom();
		com.setCon(getCon());
		ExecuteOrderReply reply = com.process(order);
		
		Acct acct = order.getSubscriber().getKundeId();
        if (reply.getErrorMessage() != null) {
            String detailError = AddSubscriberCom.makePrettyErrorMessage(reply.getErrorMessage(), reply.getXml().getResponse());
            Response response;
            response = new Response(acct, reply.getOrderId(), reply.getXml()
                , detailError, null);
            return response;
        } else {
            Response response = readSubscription(acct);
            Response sum;
            sum = new Response(acct, reply.getOrderId(), reply.getXml()
                , response.getErrorMessage()
                , response.getSmp());
            return sum;
        }
	}
	
	   public ExecuteOrderReply maintainPlan(Order order) {
	        ProvisioningCom com = new ProvisioningCom();
	        com.setCon(getCon());
	        return com.process(order);
	    }

}