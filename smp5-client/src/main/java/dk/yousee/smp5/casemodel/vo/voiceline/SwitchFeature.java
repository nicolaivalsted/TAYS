package dk.yousee.smp5.casemodel.vo.voiceline;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Oct 21, 2010 Time: 2:59:38 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.4.1.2
 * Switch Feature
 */
public class SwitchFeature extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "smp_switch_feature_pkg_std");

	public SwitchFeature(SubscriberModel model, String externalKey, VoiceService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setSwitchFeature(this);
	}
	
	//default value error
	public PropHolder sw_cos = new PropHolder(this, "sw_cos", true);
	public PropHolder sw_ocb = new PropHolder(this, "sw_ocb", true);
}
