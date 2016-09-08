package dk.yousee.smp5.casemodel.vo.base;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

public class SubContactSpec extends BasicUnit {
	public static final String FIRST_NAME = "first_name";
	public static final String MIDDLE_NAME = "mittle_name";
	public static final String LAST_NAME = "last_name";
	public static final String EMAILS_HOME_ADDRESS = "emails.home.address";
	public static final String PHONES_HOME_NUMBER = "phones.home.number";
	public static final String ISP = "isp";

	public static OrderDataLevel LEVEL = OrderDataLevel.CONTACT;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubContactSpec, "-");
	public static NickName NAME = new NickName("contact");
	public static final String EXTERNAL_KEY = "primary";

	public SubContactSpec(SubscriberModel model) {
		super(model, EXTERNAL_KEY, TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	public PropHolder first_name = new PropHolder(this, FIRST_NAME);

	public PropHolder last_name = new PropHolder(this, LAST_NAME);

	public PropHolder emails_home_address = new PropHolder(this, EMAILS_HOME_ADDRESS);

	public PropHolder phones_home_number = new PhoneNumberPropHolder(this, PHONES_HOME_NUMBER);

}
