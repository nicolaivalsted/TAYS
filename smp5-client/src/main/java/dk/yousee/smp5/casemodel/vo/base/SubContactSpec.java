package dk.yousee.smp5.casemodel.vo.base;

import dk.yousee.smp5.casemodel.vo.base.PhoneNumberPropHolder;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.Constants;
import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

public class SubContactSpec extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CONTACT;
	public static OrderDataType TYPE = new OrderDataType(
			ServicePrefix.SubContactSpec, "-");
	public static NickName NAME = new NickName("contact");
	public static final String EXTERNAL_KEY = "primary";

	public SubContactSpec(SubscriberModel model) {
		super(model, EXTERNAL_KEY, TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	public PropHolder first_name = new PropHolder(this, Constants.FIRST_NAME);
	public PropHolder mittle_name = new PropHolder(this, Constants.MIDDLE_NAME);

	public PropHolder last_name = new PropHolder(this, Constants.LAST_NAME);

	
	public PropHolder isp = new PropHolder(this, Constants.ISP);

	public PropHolder phone_cell_number = new PropHolder(this,
			Constants.PHONES_CELL_NUMBER);

	public PropHolder emails_home_address = new PropHolder(this,
			Constants.EMAILS_HOME_ADDRESS);

	public PropHolder phones_business_number = new PropHolder(this,
			Constants.PHONES_BUSINESS_NUMBER);

	public PropHolder phones_home_number = new PhoneNumberPropHolder(this,
			Constants.PHONES_HOME_NUMBER);

}
