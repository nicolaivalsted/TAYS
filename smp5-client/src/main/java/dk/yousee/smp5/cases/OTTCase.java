package dk.yousee.smp5.cases;

import org.apache.commons.lang3.StringUtils;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.ott.OTTSubscription;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

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
		private String id;
		private String ottProduct;
		private String entitlementId;
		private String serviceName;
		private String acct;
		private String linkedId;

		public String getLinkedId() {
			return linkedId;
		}

		public void setLinkedId(String linkedId) {
			this.linkedId = linkedId;
		}

		public String getAcct() {
			return acct;
		}

		public void setAcct(String acct) {
			this.acct = acct;
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

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "OTTData [id=" + id + ", ottProduct=" + ottProduct + ", entitlementId=" + entitlementId + ", serviceName=" + serviceName + ", acct=" + acct
					+ "]";
		}
	}

	public Order create(OTTData lineItem) throws BusinessException {
		ensureAcct();

		String sik = lineItem.getAcct() + "-" + lineItem.getId() + "-" + lineItem.getOttProduct();

		boolean newHasLinkedId = StringUtils.isNotBlank(lineItem.getLinkedId());

		OTTSubscription ottSubscription = getModel().alloc().OTTSubscription(sik);
		ottSubscription.sik.setValue(sik);
		ottSubscription.ott_product.setValue(lineItem.getOttProduct());
		ottSubscription.service_name.setValue(lineItem.getServiceName());
		ottSubscription.ott_entitlement_id.setValue(lineItem.getEntitlementId());
		ottSubscription.has_linked_id.setValue(String.valueOf(newHasLinkedId));
		return getModel().getOrder();
	}

	public Order update(String sik, String linkedid) throws BusinessException {
		ensureAcct();
		boolean newHasLinkedId = StringUtils.isNotBlank(linkedid);

		OTTSubscription ottSubscription = getModel().find().OTTSubscription(sik);
		ottSubscription.has_linked_id.setValue(String.valueOf(newHasLinkedId));
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
			return true;
		}
		return false;
	}

}
