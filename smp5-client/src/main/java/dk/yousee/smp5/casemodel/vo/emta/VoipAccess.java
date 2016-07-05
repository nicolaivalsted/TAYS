package dk.yousee.smp5.casemodel.vo.emta;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Oct 12, 2010 Time: 2:50:47 PM
 * Value object for eMTA Voip Data structure reference to YouSee Data Migration
 * Requirements: 5.3.2 PacketCable VoIP Access
 */
public class VoipAccess extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "emta_voice_port");

	public VoipAccess(SubscriberModel model, String externalKey, MTAService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setVoipAccess(this);
		mta_service_id.updateValue(externalKey);
	}

	public PropHolder mta_service_id = new PropHolder(this, "mta_service_id", true);
	public PropHolder mta_mac = new PropHolder(this, "mta_mac", true);
	public PropHolder mta_id = new PropHolder(this, "mta_id", true);
	public PropHolder cm_mac = new PropHolder(this, "cm_mac", false);
	public PropHolder cmts = new PropHolder(this, "cmts", false);

}
