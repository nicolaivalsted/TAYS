package dk.yousee.smp5.casemodel.vo.ott;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.Constants;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 29/10/2015 Time: 17:50:08
 */
public class OTTEntitlement extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "ott_entitlement");

	public PropHolder rate_code = new PropHolder(this, Constants.RATE_CODE, true);
	public PropHolder acct = new PropHolder(this, "acct", true);
	public PropHolder product_id = new PropHolder(this, Constants.PRODUCT_ID, true);
	public PropHolder business_position = new PropHolder(this, Constants.BUSINESS_POSITION, true);
	public PropHolder ott_product = new PropHolder(this, Constants.OTT_PRODUCT, true);
	public PropHolder uuid = new PropHolder(this, Constants.UUID, false);
	public PropHolder service_name = new PropHolder(this, Constants.SERVICE_NAME, false);

	public OTTEntitlement(SubscriberModel model, String externalKey, OTTSubscription parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setOttEntitlement(this);
	}

	public OTTSubscription getParent() {
		return (OTTSubscription) super.getParent();
	}

}
