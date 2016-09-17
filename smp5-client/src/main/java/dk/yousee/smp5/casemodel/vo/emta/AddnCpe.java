package dk.yousee.smp5.casemodel.vo.emta;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Oct 21, 2010 Time: 1:37:14 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.5.3
 * Additional CPE
 */
public class AddnCpe extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "emta_additional_cpe");

	public AddnCpe(SubscriberModel model, String externalKey, MTAService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setAddnCpe(this);
	}

	public PropHolder cpe_mac = new PropHolder(this, "cpe_mac", true);
	public PropHolder cm_mac = new PropHolder(this, "cm_mac", true);
	public PropHolder suspend_billing = new PropHolder(this, "suspend_billing");
	public PropHolder suspend_abuse = new PropHolder(this, "suspend_abuse");

}
