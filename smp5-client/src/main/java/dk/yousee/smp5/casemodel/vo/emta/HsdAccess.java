package dk.yousee.smp5.casemodel.vo.emta;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.ModemId;
import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.helpers.AssociationHolder;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Oct 12, 2010 Time: 2:42:28 PM
 * Value object for eMTA Cable Modem Data structure reference to YouSee Data
 * Migration Requirements: 5.3.1 Cable Modem/SMP HSD Access
 */
public class HsdAccess extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "emta_data_port");

	public HsdAccess(SubscriberModel model, String externalKey, MTAService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setHsdAccess(this);
		cm_service_id.updateValue(externalKey);
	}

	public PropHolder data_port_id = new PropHolder(this, "data_port_id", true);
	private PropHolder cm_ownership = new PropHolder(this, "cm_ownership", true);
	public PropHolder cm_technology = new PropHolder(this, "cm_technology");
	public PropHolder equipment_type = new PropHolder(this, "equipment_type");
	public PropHolder cm_service_id = new PropHolder(this, "cm_service_id", true);
	public PropHolder gi_address = new PropHolder(this, "gi_address", true);
	public PropHolder wifi_capable = new PropHolder(this, "wifi_capable");
	public PropHolder class_of_service = new PropHolder(this, "class_of_service", false);
	public PropHolder max_num_cpe = new PropHolder(this, "max_num_cpe");
	public PropHolder docsis_3_capable = new PropHolder(this, "docsis_3_capable", true);
	public PropHolder network_type = new PropHolder(this, "network_type", true);

	public ModemId getCmOwnership() {
		return ModemId.create(cm_ownership.getValue());
	}

	// Type.ASSOC
	public AssociationHolder service_on_address = new AssociationHolder(this, "service_on_address", SubAddressSpec.TYPE);

}
