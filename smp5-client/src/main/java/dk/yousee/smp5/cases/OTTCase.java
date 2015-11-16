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

		public OTTData(BusinessPosition businessPosition) {
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
			return "OTTData [businessPosition=" + businessPosition + ", rateCode=" + rateCode + ", ottProduct=" + ottProduct
					+ ", entitlementId=" + entitlementId + ", serviceName=" + serviceName + ", beginDate=" + beginDate + ", endDate="
					+ endDate + "]";
		}
	}

	public Order create(OTTData lineItem) throws BusinessException {
		ensureAcct();

		OTTSubscription ottSubscription = getModel().alloc().OTTSubscription(lineItem.getEntitlementId());

		ottSubscription.rate_code.setValue(lineItem.getRateCode());
		ottSubscription.business_position.setValue(lineItem.getBusinessPosition().getId());
		ottSubscription.ott_product.setValue(lineItem.getOttProduct());
		ottSubscription.service_name.setValue(lineItem.getServiceName());
		ottSubscription.ott_entitlement_id.setValue(lineItem.getEntitlementId());
		ottSubscription.begin_date.setValue(lineItem.getBeginDate());
		ottSubscription.end_date.setValue(lineItem.getEndDate());

		return getModel().getOrder();
	}

	public Order update(OTTData lineItem) throws BusinessException {
		ensureAcct();

		OTTSubscription ottSubscription = getModel().alloc().OTTSubscription(lineItem.getEntitlementId());

		if (ottSubscription == null) {
			throw new BusinessException("Update failed, OTT  service Plan was not found: for id: %s", lineItem.getEntitlementId());
		}

		if (lineItem.getBusinessPosition() != null) {
			ottSubscription.business_position.setValue(lineItem.getBusinessPosition().getId());
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
		if (!ottSubscription.ott_entitlement_id.hasValue()) {
			if (lineItem.getEntitlementId() != null) {
				ottSubscription.ott_entitlement_id.setValue(lineItem.getEntitlementId());
			}
		}
		if (lineItem.getBeginDate() != null) {
			ottSubscription.begin_date.setValue(lineItem.getBeginDate());
		}

		if (lineItem.getEndDate() != null) {
			ottSubscription.end_date.setValue(lineItem.getEndDate());
		}

		return getModel().getOrder();
	}

	public boolean delete(String entitlement) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(entitlement, Action.DELETE);
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
	private boolean buildOrderFromAction(String entitlement, Action delete) throws BusinessException {
		OTTSubscription ottSubscription = getModel().find().OTTSubscription(entitlement);
		if (ottSubscription != null) {
			ottSubscription.sendAction(Action.DELETE);
			return true;
		}else{
			throw new BusinessException("Delete failed, OTT Subscription:  entitlementId=%s  was not found", entitlement);
		}
	}

}
