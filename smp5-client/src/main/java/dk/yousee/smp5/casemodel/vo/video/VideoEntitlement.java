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
 *         Date: 30/10/2015 Time: 11:34:54
 */
public class VideoEntitlement extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "video_subscription");

	public VideoEntitlement(SubscriberModel model, String externalKey, VideoSubscription parent, VideoEvent parent2) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		if (parent != null) {
			parent.setVideoEntitlement(this);
		}
		if (parent2 != null) {
			parent.setVideoEntitlement(this);
		}
	}

	public PropHolder video_entitlement_id = new PropHolder(this, "video_entitlement_id", true);
	public PropHolder video_entitlement_type = new PropHolder(this, "video_entitlement_type", true);
	public PropHolder video_entitlement_uniqueness_check = new PropHolder(this, "video_entitlement_uniqueness_check", true);
	public PropHolder provider = new PropHolder(this, "provider", true);
	public PropHolder cable_unit = new PropHolder(this, "cabl_unit", true);
	public PropHolder business_position = new PropHolder(this, "business_position", true);

	public VideoEvent getParent2() {
		return (VideoEvent) super.getParent();
	}

	public VideoSubscription getParent() {
		return (VideoSubscription) super.getParent();
	}

}
