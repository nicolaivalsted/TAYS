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
 *         Date: Sep 15, 2016 Time: 1:44:24 PM
 */
public class AppSubscription extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "video_subscription");

	public AppSubscription(SubscriberModel model, String externalKey, VideoServicePlan parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.getAppSubscriptions().add(this);
	}

	public PropHolder channel_id = new PropHolder(this, "channel_id", true);
	public PropHolder name = new PropHolder(this, "name", true);
	public PropHolder sik = new PropHolder(this, "sik", true);

	public AppSubscription getParent() {
		return (AppSubscription) super.getParent();
	}

}
