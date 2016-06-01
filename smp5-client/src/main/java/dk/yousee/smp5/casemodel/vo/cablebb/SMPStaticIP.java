package dk.yousee.smp5.casemodel.vo.cablebb;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.emta.StdCpe;
import dk.yousee.smp5.casemodel.vo.helpers.AssociationHolder;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Oct 21, 2010 Time: 1:43:29 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.5.4 SMP
 * Static IP Service
 */
public class SMPStaticIP extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "static_ip");

	public SMPStaticIP(SubscriberModel model, String externalKey, CableBBService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setSmpStaticIP(this);
		staticip_service_id.updateValue(externalKey);
	}

	// Type.FEATURE
	public PropHolder staticip_service_id = new PropHolder(this, "staticip_service_id", true);
	public PropHolder static_ip_address = new PropHolder(this, "static_ip", true);
	public PropHolder staticip_product_code = new PropHolder(this, "staticip_product_code", true);
	public PropHolder staticip_cpe_mac = new PropHolder(this, "cpe_mac");
	public PropHolder static_gi_address = new PropHolder(this, "gi_address");

	// Type.ASSOC
	public AssociationHolder static_ip_has_std_cpe = new AssociationHolder(this, "static_ip_has_std_cpe", StdCpe.TYPE);
}
