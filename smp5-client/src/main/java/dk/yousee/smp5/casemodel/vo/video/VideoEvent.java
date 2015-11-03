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
 *         Date: 26/10/2015 Time: 19:13:28
 */
public class VideoEvent extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "video_event");

	public VideoEvent(SubscriberModel model, String externalKey, VideoServicePlan parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.getVideoEvents().add(this);
	}

	public PropHolder video_entitlement_id = new PropHolder(this, "video_entitlement_id", true);

	public VideoServicePlan getParent() {
		return (VideoServicePlan) super.getParent();
	}

}
