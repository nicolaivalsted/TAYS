package dk.yousee.smp5.casemodel.vo.video;

import java.util.List;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 26/10/2015 Time: 17:31:17
 */
public class VideoComposedService extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "video_services_composed");
	public static NickName NAME = new NickName("video");

	public VideoComposedService(SubscriberModel model, String externalKey) {
		super(model, externalKey, TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	List<VideoServicePlan> videoServicePlans;

	public List<VideoServicePlan> getVideoServicePlans() {
		return videoServicePlans;
	}

	public void setVideoServicePlans(List<VideoServicePlan> videoServicePlans) {
		this.videoServicePlans = videoServicePlans;
	}

}