package dk.yousee.smp5.casemodel.vo.smartcard;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 18/02/2016 Time: 12:09:51
 */
public class SmartCard extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "smart_card");

	public PropHolder sik = new PropHolder(this, "sik", true);
	public PropHolder pacos = new PropHolder(this, "pacos", true);
	public PropHolder serialNumber = new PropHolder(this, "serialNumber", true);
	public PropHolder pinCode = new PropHolder(this, "pinCode", true);
	public PropHolder vodPayee =  new PropHolder(this, "vodPayee", true);
	public PropHolder modifyDate = new PropHolder(this, "modifyDate", true);
	public PropHolder parentalPin = new PropHolder(this, "parentalPin", true);
	public PropHolder viAction = new PropHolder(this, "viAction", true);

	public SmartCard(SubscriberModel model, String externalKey, SmartCardService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.getSmartCards().add(this);
	}

	public SmartCardService getParent() {
		return (SmartCardService) super.getParent();
	}

}
