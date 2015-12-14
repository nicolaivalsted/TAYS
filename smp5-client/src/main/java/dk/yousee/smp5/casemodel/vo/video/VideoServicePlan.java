package dk.yousee.smp5.casemodel.vo.video;

import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 26/10/2015 Time: 18:54:20
 */
public class VideoServicePlan extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "video_service_plan");

	public VideoServicePlan(SubscriberModel model, String externalKey, VideoComposedService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setVideoServicePlan(this);
	}

	VideoServicePlanAttributes videoServicePlanAttributes;

	List<VideoSubscription> videoSubscriptions = new ArrayList<VideoSubscription>();

	public VideoComposedService getParent() {
		return (VideoComposedService) super.getParent();
	}

	public VideoServicePlanAttributes getVideoServicePlanAttributes() {
		return videoServicePlanAttributes;
	}

	public void setVideoServicePlanAttributes(VideoServicePlanAttributes videoServicePlanAttributes) {
		this.videoServicePlanAttributes = videoServicePlanAttributes;
	}

	public List<VideoSubscription> getVideoSubscriptions() {
		return videoSubscriptions;
	}

	public void setVideoSubscriptions(List<VideoSubscription> videoSubscriptions) {
		this.videoSubscriptions = videoSubscriptions;
	}

}
