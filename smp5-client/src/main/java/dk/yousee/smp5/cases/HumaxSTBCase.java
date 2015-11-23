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
		private String oldSerialNumber;
		private String acct;
		private String servicePlanId;
		private String model;

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

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public String getOldSerialNumber() {
			return oldSerialNumber;
		}

		public void setOldSerialNumber(String oldSerialNumber) {
			this.oldSerialNumber = oldSerialNumber;
		}

		@Override
		public String toString() {
			return "STBData [serialNumber=" + serialNumber + ", oldSerialNumber=" + oldSerialNumber + ", acct=" + acct + ", servicePlanId="
					+ servicePlanId + ", model=" + model + "]";
		}
	}

	public Order create(STBData lineItem) throws BusinessException {
		ensureAcct();
		STBCas stbCas;
		if(lineItem.oldSerialNumber.equals("")){
			stbCas = getModel().alloc().STBCas(lineItem.serialNumber);
		}else{
			stbCas = getModel().alloc().STBCas(lineItem.oldSerialNumber);
		}
		
		stbCas.acct.setValue(lineItem.getAcct());
		stbCas.serialNumber.setValue(lineItem.getSerialNumber());
		stbCas.model.setValue(lineItem.getModel());

		if (lineItem.getServicePlanId() != null && !lineItem.getServicePlanId().equals("")) {

			VideoServicePlanAttributes videoServicePlanAttributes = getModel().find().VideoServicePlanAttributes(
					lineItem.getServicePlanId());
			if (videoServicePlanAttributes != null) {
				videoServicePlanAttributes.video_service_defn_has_cas.add(stbCas);
			} else {
				throw new BusinessException(" Video Service Plan: %s not dound", lineItem.getServicePlanId());
			}
		}
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
	 * @throws BusinessException
	 */
	private boolean buildOrderFromAction(String serialNumber, Action delete) throws BusinessException {
		VideoCPE videoCPE = getModel().find().VideoCPE(serialNumber);
		if (videoCPE != null) {
			videoCPE.sendAction(Action.DELETE);
			return true;
		} else {
			throw new BusinessException("Delete failed, Video CPE:  serialNumber=%s  was not found", serialNumber);
		}
	}

}
