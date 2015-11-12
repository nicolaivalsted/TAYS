package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPE;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

/**
 * @author m64746
 *
 *         Date: 20/10/2015 Time: 12:42:14
 */
public class HumaxSTBCase extends AbstractCase {

	public HumaxSTBCase(SubscriberModel model, OrderService service) {
		super(model, service);
	}

	public HumaxSTBCase(Acct acct, OrderService service) {
		super(acct, service);
	}

	public HumaxSTBCase(SubscriberCase customerCase, boolean keepModel) {
		super(selectModel(customerCase.getModel(), keepModel), customerCase.getService());
	}

	public static class STBData {
		private String serialNumber;
		private String acct;
		private String servicePlanId;

		public String getServicePlanId() {
			return servicePlanId;
		}

		public void setServicePlanId(String servicePlanId) {
			this.servicePlanId = servicePlanId;
		}

		public String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}

		public String getAcct() {
			return acct;
		}

		public void setAcct(String acct) {
			this.acct = acct;
		}

		@Override
		public String toString() {
			return "STBData [serialNumber=" + serialNumber + ", acct=" + acct + ", servicePlanId=" + servicePlanId + "]";
		}
	}

	public Order create(STBData lineItem) throws BusinessException {
		ensureAcct();

		STBCas stbCas = getModel().alloc().STBCas(lineItem.serialNumber);

		stbCas.acct.setValue(lineItem.getAcct());
		stbCas.serialNumber.setValue(lineItem.getSerialNumber());

		if (lineItem.getServicePlanId() != null && !lineItem.getServicePlanId().equals("")) {

			VideoServicePlanAttributes videoServicePlanAttributes = getModel().find().VideoServicePlanAttributes(
					lineItem.getServicePlanId());
			if (stbCas != null) {
				videoServicePlanAttributes.video_definition_has_cpe_conditional.add(stbCas);
			}
		}

		return getModel().getOrder();
	}

	public Order update(STBData lineItem) throws BusinessException {
		ensureAcct();
		return getModel().getOrder();
	}

	public boolean delete(String serialNumber) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(serialNumber, Action.DELETE);
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
	private boolean buildOrderFromAction(String serialNumber, Action delete) {
		VideoCPE videoCPE = getModel().find().VideoCPE(serialNumber);
		if (videoCPE != null) {
			videoCPE.sendAction(Action.DELETE);
			return true;
		}
		return false;
	}

}
