package dk.yousee.smp5.casemodel.vo.video;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 26/10/2015 Time: 18:54:40
 */
public class VideoServicePlanAttributes extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "video_access");

	public VideoServicePlanAttributes(SubscriberModel model, String externalKey, VideoServicePlan parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		this.video_service_plan_id.setValue(externalKey);
		parent.setVideoServicePlanAttributes(this);
	}

	public PropHolder video_service_plan_id = new PropHolder(this, "video_service_plan_id", true);
	public PropHolder video_service_plan_uniqueness_check = new PropHolder(this, "video_service _plan_uniqueness_check", true);
	public PropHolder ippv_entitled = new PropHolder(this, "ippv_entitled", true);
	public PropHolder vod_entitled = new PropHolder(this, "vod_entitled", true);
	public PropHolder interactive_service_entitled = new PropHolder(this, "interactive_service_entitled", true);
	public PropHolder analog_entitled = new PropHolder(this, "analog_entitled", true);

	public BusinessPosition getPosition() {
		return BusinessPosition.create(video_service_plan_id.getValue());
	}
}
