package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.stb.CpeConditionalAccess;
import dk.yousee.smp5.order.model.Acct;
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
		private BusinessPosition businessPosition;
		private String manufacturer;
		private String model;
		private String macAddress;
		private String serialNumber;
		private String acct;

		public STBData(BusinessPosition position) {
			super();
			this.businessPosition = position;
		}

		public BusinessPosition getBusinessPosition() {
			return businessPosition;
		}

		public void setBusinessPosition(BusinessPosition businessPosition) {
			this.businessPosition = businessPosition;
		}

		public String getManufacturer() {
			return manufacturer;
		}

		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public String getMacAddress() {
			return macAddress;
		}

		public void setMacAddress(String macAddress) {
			this.macAddress = macAddress;
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
	}

	public Order create(STBData lineItem) throws BusinessException {
		ensureAcct();

		CpeConditionalAccess cpeConditionalAccess = getModel().alloc().CpeConditionalAccess(lineItem.macAddress);

		cpeConditionalAccess.acct.setValue(lineItem.getAcct());
		cpeConditionalAccess.macAddress.setValue(lineItem.getMacAddress());
		cpeConditionalAccess.serialNumber.setValue(lineItem.getSerialNumber());
		cpeConditionalAccess.manufacturer.setValue(lineItem.getManufacturer());
		cpeConditionalAccess.model.setValue(lineItem.getModel());

		return getModel().getOrder();
	}

	public Order update(STBData lineItem) throws BusinessException {
		ensureAcct();
		// TODO
		return getModel().getOrder();
	}

}
