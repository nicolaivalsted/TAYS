package dk.yousee.smp5.cases;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.smartcard.SmartCard;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

/**
 * @author m64746
 *
 *         Date: 18/02/2016 Time: 12:07:18
 */
public class SmartCardCase extends AbstractCase {

	public SmartCardCase(SubscriberModel model, OrderService service) {
		super(model, service);
	}

	public static class SmartCardData {
		private String sik;
		private String pacos;
		private String serialNumber;
		private String pinCode;
		private String modifyDate;
		private String vodPayee;
		private String parentalPin;
		private String viAction;

		public String getSik() {
			return sik;
		}

		public void setSik(String sik) {
			this.sik = sik;
		}

		public String getPacos() {
			return pacos;
		}

		public void setPacos(String pacos) {
			this.pacos = pacos;
		}

		public String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}

		public String getPinCode() {
			return pinCode;
		}

		public void setPinCode(String pinCode) {
			this.pinCode = pinCode;
		}

		public String getModifyDate() {
			return modifyDate;
		}

		public void setModifyDate(String modifyDate) {
			this.modifyDate = modifyDate;
		}

		public String getParentalPin() {
			return parentalPin;
		}

		public void setParentalPin(String parentalPin) {
			this.parentalPin = parentalPin;
		}

		public String getViAction() {
			return viAction;
		}

		public void setViAction(String viAction) {
			this.viAction = viAction;
		}

		public String getVodPayee() {
			return vodPayee;
		}

		public void setVodPayee(String vodPayee) {
			this.vodPayee = vodPayee;
		}

		@Override
		public String toString() {
			return "SmartCardData [sik=" + sik + ", pacos=" + pacos + ", serialNumber=" + serialNumber + ", pinCode=" + pinCode
					+ ", modifyDate=" + modifyDate + ", vodPayee=" + vodPayee + ", parentalPin=" + parentalPin + ", viAction=" + viAction
					+ "]";
		}
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
		SmartCard smartCard = getModel().find().SmartCard(sik);
		if (smartCard != null) {
			smartCard.sendAction(Action.DELETE);
			return true;
		}
		return false;
	}

	public Order create(SmartCardData lineItem) throws BusinessException {
		validateInput(lineItem);

		SmartCard smartCard = getModel().add().SmartCard(getAcct().toString());

		if (!lineItem.getPacos().equals("")) {
			smartCard.pacos.setValue(lineItem.getPacos());
		}

		if (lineItem.getViAction().equals("VIAPIN")) {
			smartCard.parentalPin.setValue(lineItem.getPinCode().equals("") ? "" : lineItem.getPinCode());
		} else {
			smartCard.pinCode.setValue(lineItem.getPinCode().equals("") ? "" : lineItem.getPinCode());
		}

		smartCard.serialNumber.setValue(lineItem.getSerialNumber());
		smartCard.vodPayee.setValue(lineItem.getVodPayee());
		smartCard.sik.setValue(lineItem.getSik());
		smartCard.viAction.setValue(lineItem.getViAction());

		return getModel().getOrder();
	}

	public Order update(SmartCardData lineItem) throws BusinessException {
		validateInput(lineItem);

		SmartCard smartCard = getModel().find().SmartCard(lineItem.getSik());

		smartCard.pacos.setValue(lineItem.getPacos());

		if (lineItem.getViAction().equals("VIAPIN")) {
			smartCard.parentalPin.setValue(lineItem.getPinCode().equals("") ? "" : lineItem.getPinCode());
		} else {
			smartCard.pinCode.setValue(lineItem.getPinCode().equals("") ? "" : lineItem.getPinCode());
		}

		if (!lineItem.getSerialNumber().equals(smartCard.serialNumber.getValue())) {
			smartCard.serialNumber.setValue(lineItem.getPinCode());
		}

		if (!lineItem.getVodPayee().equals(smartCard.vodPayee.getValue())) {
			smartCard.vodPayee.setValue(lineItem.getVodPayee());
		}

		smartCard.viAction.setValue(lineItem.getViAction());
		smartCard.modifyDate.setValue(generateModifyDate());
		smartCard.sendAction(Action.UPDATE);

		return getModel().getOrder();
	}

	public static String generateModifyDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
		String dateFinal = sdf.format(new Date()) + "-" + new Random().nextInt(5000);
		return dateFinal;
	}

	/**
	 * @param lineItem
	 * @throws BusinessException
	 */
	private void validateInput(SmartCardData lineItem) throws BusinessException {
		if (lineItem.getViAction().equals("")) {
			throw new BusinessException("viCommand is required for SmartCard");
		}

		if (lineItem.getSerialNumber().equals("")) {
			throw new BusinessException("Serial Number is required for SmartCards");
		}
	}

}
