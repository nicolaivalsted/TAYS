package dk.yousee.smp5.casemodel.vo.ott;

import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.order.model.Constants;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;

/**
 * @author m64746
 *
 *         Date: 21/10/2015 Time: 12:04:15
 */
public class OTTSubscription extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "ott_subscription");

	public PropHolder service_name = new PropHolder(this, Constants.SERVICE_NAME, true);
	public PropHolder rate_code = new PropHolder(this, Constants.RATE_CODE, true);
	public PropHolder product_id = new PropHolder(this, Constants.PRODUCT_ID, true);
	public PropHolder business_position = new PropHolder(this, Constants.BUSINESS_POSITION, true);
	public PropHolder ott_product = new PropHolder(this, Constants.OTT_PRODUCT, true);
	public PropHolder uuid = new PropHolder(this, Constants.UUID, false);

	public OTTSubscription(SubscriberModel model, String externalKey, BusinessPosition position, OTTService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		this.business_position.setValue(position.getId());
		parent.getOttSubscriptions().add(this);
	}

	public OTTService getParent() {
		return (OTTService) super.getParent();
	}

	public BusinessPosition getPosition() {
		return BusinessPosition.create(business_position.getValue());
	}
}