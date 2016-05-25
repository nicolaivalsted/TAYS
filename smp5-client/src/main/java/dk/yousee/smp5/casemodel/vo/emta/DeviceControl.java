package dk.yousee.smp5.casemodel.vo.emta;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

public class DeviceControl extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "emta_device_control");

	public DeviceControl(SubscriberModel model, String externalKey, MTAService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setDeviceControl(this);
	}

	public PropHolder gi_address = new PropHolder(this, "gi_address", true);
	public PropHolder voip_protocol = new PropHolder(this, "voip_protocol", true);
	public PropHolder quality_of_svc = new PropHolder(this, "quality_of_svc", true);
	public PropHolder dhcp_rules = new PropHolder(this, "cm_mac", true);
	public PropHolder max_data_port_num = new PropHolder(this, "max_data_port_num");
	public PropHolder max_voice_port_num = new PropHolder(this, "max_voice_port_num", true);
	public PropHolder ownership = new PropHolder(this, "ownership", true);
	public PropHolder manufacturer = new PropHolder(this, "manufacturer", true);
	public PropHolder model = new PropHolder(this, "model", true);
	public PropHolder serial_number = new PropHolder(this, "serial_number", true);
	public PropHolder device_id2 = new PropHolder(this, "device_id2", true);
	public PropHolder device_id1 = new PropHolder(this, "device_id1", true);
	public PropHolder equipment_type = new PropHolder(this, "equipment_type", true);
	public PropHolder device_nickname = new PropHolder(this, "device_nickname", true);

}
