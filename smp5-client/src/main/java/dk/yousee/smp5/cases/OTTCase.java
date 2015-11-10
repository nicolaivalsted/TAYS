package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
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
		private BusinessPosition businessPosition;
		private String rateCode;
		private String productId;
		private String ottProduct;
		private String entitlementId;
		private String serviceName;
		private String uniquenessCheck;

		/**
		 * @param businessPosition
		 */
		public OTTData(BusinessPosition businessPosition) {
			super();
			this.businessPosition = businessPosition;
		}

		public BusinessPosition getBusinessPosition() {
			return businessPosition;
		}

		public void setBusinessPosition(BusinessPosition businessPosition) {
			this.businessPosition = businessPosition;
		}

		public String getRateCode() {
			return rateCode;
		}

		public void setRateCode(String rateCode) {
			this.rateCode = rateCode;
		}

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
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

		public String getUniquenessCheck() {
			return uniquenessCheck;
		}

		public void setUniquenessCheck(String uniquenessCheck) {
			this.uniquenessCheck = uniquenessCheck;
		}

		@Override
		public String toString() {
			return "OTTData [businessPosition=" + businessPosition + ", rateCode=" + rateCode + ", productId=" + productId
					+ ", ottProduct=" + ottProduct + ", entitlementId=" + entitlementId + ", serviceName=" + serviceName
					+ ", uniquenessCheck=" + uniquenessCheck + "]";
		}
	}

	public Order create(OTTData lineItem) throws BusinessException {
		ensureAcct();

		OTTSubscription ottSubscription = getModel().alloc().OTTSubscription(lineItem.getRateCode());

		ottSubscription.rate_code.setValue(lineItem.getRateCode());
		ottSubscription.business_position.setValue(lineItem.getBusinessPosition().getId());
		ottSubscription.ott_product.setValue(lineItem.getOttProduct());
		ottSubscription.product_id.setValue(lineItem.getProductId());
		ottSubscription.service_name.setValue(lineItem.getServiceName());
		ottSubscription.entitlement_uniqueness_check.setValue(lineItem.getUniquenessCheck());
		ottSubscription.ott_entitlement_id.setValue(lineItem.getEntitlementId());

		return getModel().getOrder();
	}

	public Order update(OTTData lineItem) throws BusinessException {
		ensureAcct();

		OTTSubscription ottSubscription = getModel().alloc().OTTSubscription(lineItem.getRateCode());

		if (ottSubscription == null) {
			throw new BusinessException("Update failed, OTT  service Plan was not found: for raste code: %s", lineItem.getRateCode());
		}

		if (lineItem.getBusinessPosition() != null) {
			ottSubscription.business_position.setValue(lineItem.getBusinessPosition().getId());
		}

		if (lineItem.getProductId() != null) {
			ottSubscription.product_id.setValue(lineItem.getProductId());
		}

		if (lineItem.getRateCode() != null) {
			ottSubscription.rate_code.setValue(lineItem.getRateCode());
		}
		if (lineItem.getOttProduct() != null) {
			ottSubscription.ott_product.setValue(lineItem.getOttProduct());
		}
		if (lineItem.getServiceName() != null) {
			ottSubscription.service_name.setValue(lineItem.getServiceName());
		}
		if (lineItem.getEntitlementId() != null) {
			ottSubscription.ott_entitlement_id.setValue(lineItem.getEntitlementId());
		}
		if (lineItem.getUniquenessCheck() != null) {
			ottSubscription.entitlement_uniqueness_check.setValue(lineItem.getUniquenessCheck());
		}

		return getModel().getOrder();
	}

	public boolean delete(String rateCode) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(rateCode, Action.DELETE);
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
	 */
	private boolean buildOrderFromAction(String rateCode, Action delete) {
		OTTSubscription ottSubscription = getModel().find().OTTSubscription(rateCode);
		if (ottSubscription != null) {
			ottSubscription.sendAction(Action.DELETE);
			return true;
		}
		return false;
	}

}
