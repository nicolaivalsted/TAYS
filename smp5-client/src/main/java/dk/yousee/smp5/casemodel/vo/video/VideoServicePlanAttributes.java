package dk.yousee.smp5.casemodel.vo.video;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.helpers.AssociationHolder;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
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

	public BusinessPosition getPosition() {
		return BusinessPosition.create(video_service_plan_id.getValue());
	}

	// Type.ASSOC
	public AssociationHolder video_definition_has_cpe_conditional = new AssociationHolder(this, "video_definition_has_cpe_conditional",
			STBCas.TYPE);

}
