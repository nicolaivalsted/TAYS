package dk.yousee.smp5.casemodel.vo.emta;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Oct 12, 2010 Time: 3:08:58 PM
 * Value object for STD CPE Data structure reference to YouSee Data Migration
 * Requirements: 5.5.2 SMP Standard CPE
 */
public class StdCpe extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "emta_standard_cpe");

	public StdCpe(SubscriberModel model, String externalKey, MTAService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setStdCpe(this);
		cpe_service_id.updateValue(externalKey);
	}

	// Type.FEATURE
	public PropHolder cpe_service_id = new PropHolder(this, "cpe_service_id", true);
	public PropHolder cpe_mac = new PropHolder(this, "cpe_mac", true);
	public PropHolder cm_mac = new PropHolder(this, "cm_mac", true);
	/**
	 * Suspend reason code and text
	 */
	public PropHolder suspend_billing = new PropHolder(this, "suspend_billing");
	public PropHolder suspend_abuse = new PropHolder(this, "suspend_abuse");

}
