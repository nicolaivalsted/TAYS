package dk.yousee.smp5.casemodel.vo.video;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 26/10/2015 Time: 18:54:55
 */
public class VideoSubscription extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "video_subscription");

	public VideoSubscription(SubscriberModel model, String externalKey, VideoServicePlan parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.getVideoSubscriptions().add(this);
	}

	public PropHolder video_entitlement_id = new PropHolder(this, "video_entitlement_id", true);
	public PropHolder video_entitlement_type = new PropHolder(this, "video_entitlement_type", true);
	public PropHolder video_entitlement_uniqueness_check = new PropHolder(this, "video_entitlement_uniqueness_check", true);

	public PropHolder provider = new PropHolder(this, "provider", true);
	public PropHolder cable_unit = new PropHolder(this, "cabl_unit", true);
	public PropHolder business_position = new PropHolder(this, "business_position", true);

	public VideoServicePlan getParent() {
		return (VideoServicePlan) super.getParent();
	}
}
