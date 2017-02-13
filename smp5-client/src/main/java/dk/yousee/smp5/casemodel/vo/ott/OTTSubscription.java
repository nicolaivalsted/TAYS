package dk.yousee.smp5.casemodel.vo.ott;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 21/10/2015 Time: 12:04:15
 */
public class OTTSubscription extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "ott_subscription");

	public OTTSubscription(SubscriberModel model, String externalKey, OTTService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.getOttSubscriptions().add(this);
	}

	public PropHolder sik = new PropHolder(this, "sik", true);
	public PropHolder ott_product = new PropHolder(this, "ott_product", true);
	public PropHolder service_name = new PropHolder(this, "service_name", true);
	public PropHolder ott_entitlement_id = new PropHolder(this, "ott_entitlement_id", true);
	public PropHolder linkedid = new PropHolder(this, "linkedid", false);

	public OTTService getParent() {
		return (OTTService) super.getParent();
	}

}