package dk.yousee.smp5.casemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp5.casemodel.vo.helpers.Add;
import dk.yousee.smp5.casemodel.vo.helpers.Alloc;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.Find;
import dk.yousee.smp5.casemodel.vo.helpers.Key;
import dk.yousee.smp5.casemodel.vo.helpers.Parse;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.Response;
import dk.yousee.smp5.order.model.ServiceProviderEnum;
import dk.yousee.smp5.order.model.Subscriber;

public class SubscriberModel implements Serializable {

	private static final long serialVersionUID = 5186104694621864740L;
	private Subscriber subscriber;
	private Response response = null;
	private ServiceProviderEnum provider = ServiceProviderEnum.YouSee;
	private List<BasicUnit> serviceLevelUnit = new ArrayList<BasicUnit>();
	private Key key = null;
	private Find find = null;
	private Alloc alloc = null;
	private Add add = null;
	private Order order = null;

	/**
	 * Constructs the model based on a response
	 *
	 * @param response
	 *            - should be directly return value from a service call.
	 */
	public SubscriberModel(Response response) {
		this(response.getAcct());
		this.response = response;
		new Parse(this).buildSubscriberModel(response.getSmp());
	}

	/**
	 * Constructs the model based on an account number, ready for
	 * addSubscription and ready for getting response
	 *
	 * @param acct
	 *            account to create model for
	 */
	public SubscriberModel(Acct acct) {
		subscriber = new Subscriber();
		subscriber.setKundeId(acct);
	}

	/**
	 * The account this response was made from
	 *
	 * @return account - customers 9 digit account number
	 */
	public Acct getAcct() {
		return subscriber.getKundeId();
	}

	public Response getResponse() {
		return response;
	}

	/**
	 * Does the customer exist in Sigma yet
	 *
	 * @return true means the customer is registered
	 */
	public boolean customerExists() {
		return getResponse().getSmp() != null;
	}

	public ServiceProviderEnum getProvider() {
		return provider;
	}

	/**
	 * Assign the provider if it hs not YouSee
	 *
	 * @param provider
	 *            another provider than YouSee
	 */
	public void setProvider(ServiceProviderEnum provider) {
		this.provider = provider;
	}

	public List<BasicUnit> getServiceLevelUnit() {
		return serviceLevelUnit;
	}

	public List<BasicUnit> filterProgress() {
		List<BasicUnit> res = null;
		for (BasicUnit unit : getServiceLevelUnit()) {
			List<BasicUnit> progress = unit.filterProgress();
			if (progress != null) {
				if (res == null)
					res = new ArrayList<BasicUnit>();
				res.addAll(progress);
			}
		}
		return res;
	}

	/**
	 * Helper for generating keys to plans etc
	 *
	 * @return the key helper
	 */
	public Key key() {
		if (key == null)
			key = new Key(this);
		return key;
	}

	/**
	 * Helper for finding Plans etc
	 *
	 * @return the find helper
	 */
	public Find find() {
		if (find == null)
			find = new Find(this, serviceLevelUnit);
		return find;
	}

	/**
	 * Helper for allocating ServicePlans etc
	 *
	 * @return the allocation helper
	 */
	public Alloc alloc() {
		if (alloc == null)
			alloc = new Alloc(this);
		return alloc;
	}

	/**
	 * Helper for making adding ServicePlans etc
	 *
	 * @return the allocation helper
	 */
	public Add add() {
		if (add == null)
			add = new Add(this);
		return add;
	}

	public Order getOrder() {
		if (order == null) {
			order = new Order();
			order.setAsynchronous(false);
			order.setType(Order.TYPE_PROVISIONING);
			order.setSubscriber(subscriber);
		}
		if (order.getExternalKey() == null && response != null && response.getSmp() != null) {
			order.setExternalKey(response.getSmp().getExternalKey());
		}
		if (order.getExternalKey() == null) {
			order.setExternalKey(key().SubscriberExternalKey(subscriber.getKundeId()));
		}
		return order;
	}

	/**
	 * Does the model contain an order to send to sigma ?
	 * 
	 * @return true means yes
	 */
	public boolean hasOrder() {
		return order != null;
	}
	public boolean hasData(){
		return order.getOrderData().size() == 0 ? false : true;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
	
}
