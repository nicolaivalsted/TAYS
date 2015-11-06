package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.ott.OTTService;
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
		private String acct;

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

		public String getAcct() {
			return acct;
		}

		public void setAcct(String acct) {
			this.acct = acct;
		}

		@Override
		public String toString() {
			return "OTTData [businessPosition=" + businessPosition + ", rateCode=" + rateCode + ", productId=" + productId
					+ ", ottProduct=" + ottProduct + ", acct=" + acct + "]";
		}

	}

	public Order create(OTTData lineItem) throws BusinessException {
		ensureAcct();

		OTTSubscription ottSubscription = getModel().alloc().OTTSubscription(lineItem.getRateCode());

		ottSubscription.rate_code.setValue(lineItem.getRateCode());
		ottSubscription.business_position.setValue(lineItem.getBusinessPosition().getId());
		ottSubscription.ott_product.setValue(lineItem.getOttProduct());
		ottSubscription.product_id.setValue(lineItem.getProductId());
		ottSubscription.acct.setValue(lineItem.getAcct());

		return getModel().getOrder();
	}

	public Order update(OTTData lineItem) throws BusinessException {
		ensureAcct();

		OTTSubscription ottSubscription = getModel().find().OTTSubscription(lineItem.getRateCode());

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

		return getModel().getOrder();
	}

	public boolean delete(BusinessPosition position) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(position, Action.DELETE);
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
	private boolean buildOrderFromAction(BusinessPosition position, Action delete) {
		OTTService ottService = getModel().find().OTTService();
		if (ottService != null) {
			ottService.sendAction(Action.DELETE);
			return true;
		}
		return false;
	}

}
