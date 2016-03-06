package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
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
		private String sik;
		private String serialNumber;
		private String manufacturer;
		private String chipId;

		public String getChipId() {
			return chipId;
		}

		public void setChipId(String chipId) {
			this.chipId = chipId;
		}

		public String getSik() {
			return sik;
		}

		public void setSik(String sik) {
			this.sik = sik;
		}

		public String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}

		public String getManufacturer() {
			return manufacturer;
		}

		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}

		@Override
		public String toString() {
			return "STBData [sik=" + sik + ", serialNumber=" + serialNumber + ", manufacturer=" + manufacturer + ", chipId=" + chipId + "]";
		}
	}

	public Order create(STBData lineItem) throws BusinessException {
		ensureAcct();
		STBCas stbCas = getModel().alloc().STBCas(lineItem.getSik());

		stbCas.serialNumber.setValue(lineItem.getSerialNumber().toUpperCase());
		stbCas.manufacturer.setValue(lineItem.getManufacturer());
		stbCas.chipid.setValue(lineItem.getChipId().toUpperCase());
		stbCas.sik.setValue(lineItem.getSik());

		VideoServicePlanAttributes videoServicePlanAttributes = getModel().find().VideoServicePlanAttributes();
		videoServicePlanAttributes.video_service_defn_has_cas.add(stbCas);
		return getModel().getOrder();
	}

	public boolean sendAction(String id, Action action) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(id, action);
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
	private boolean buildOrderFromAction(String id, Action action) throws BusinessException {
		STBCas stbCas = getModel().find().STBCas(id);
		if (stbCas != null) {
			stbCas.sendAction(action);
		}
		return true;
	}

}
