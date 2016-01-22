package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.ott.OTTSubscription;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;
import dk.yousee.smp5.order.util.OrderHelper;

/**
 * @author m64746
 *
 *         Date: 20/10/2015 Time: 12:40:58
 */
public class OTTCase extends AbstractCase {

	public OTTCase(SubscriberModel model, OrderService service) {
		super(model, service);
	}

	public OTTCase(Acct acct, OrderService service) {
		super(acct, service);
	}

	public OTTCase(SubscriberCase customerCase, boolean keepModel) {
		super(selectModel(customerCase.getModel(), keepModel), customerCase.getService());
	}

	/**
	 * Inner class that holds the contract between CRM and SMP
	 */
	public static class OTTData {
		private String sik;
		private String ottProduct;
		private String entitlementId;
		private String serviceName;
		private String beginDate;
		private String endDate;

		public String getBeginDate() {
			return beginDate;
		}

		public void setBeginDate(String beginDate) {
			this.beginDate = beginDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getSik() {
			return sik;
		}

		public void setSik(String sik) {
			this.sik = sik;
		}

		public String getOttProduct() {
			return ottProduct;
		}

		public void setOttProduct(String ottProduct) {
			this.ottProduct = ottProduct;
		}

		public String getEntitlementId() {
			return entitlementId;
		}

		public void setEntitlementId(String entitlementId) {
			this.entitlementId = entitlementId;
		}

		public String getServiceName() {
			return serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		@Override
		public String toString() {
			return "OTTData [sik=" + sik + ", ottProduct=" + ottProduct + ", entitlementId=" + entitlementId + ", serviceName="
					+ serviceName + ", beginDate=" + beginDate + ", endDate=" + endDate + "]";
		}
	}

	public Order create(OTTData lineItem) throws BusinessException {
		ensureAcct();

		OTTSubscription ottSubscription = getModel().alloc().OTTSubscription(lineItem.getSik());

		ottSubscription.sik.setValue(lineItem.getSik());
		ottSubscription.ott_product.setValue(lineItem.getOttProduct());
		ottSubscription.service_name.setValue(lineItem.getServiceName());
		ottSubscription.ott_entitlement_id.setValue(lineItem.getEntitlementId());

		if (!lineItem.getBeginDate().equals("")) {
			ottSubscription.begin_date.setValue(OrderHelper.generateOrderDateStringFromString(lineItem.getBeginDate()));
		}
		if (!lineItem.getEndDate().equals("")) {
			ottSubscription.end_date.setValue(OrderHelper.generateOrderDateStringFromString(lineItem.getEndDate()));
		}

		return getModel().getOrder();
	}

	public boolean delete(String sik) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(sik, Action.DELETE);
		return res;
	}

	/**
	 * Constructs an order from action change
	 *
	 * @param position
	 *            selected service plan instance (key is modemId)
	 * @param action
	 *            the action to send to the subscription
	 * @return true if anything to do
	 * @throws BusinessException
	 */
	private boolean buildOrderFromAction(String sik, Action delete) throws BusinessException {
		OTTSubscription ottSubscription = getModel().find().OTTSubscription(sik);
		if (ottSubscription != null) {
			ottSubscription.sendAction(Action.DELETE);
		}
		return true;
	}

}
