package dk.yousee.smp5.casemodel.vo.video;

import dk.yousee.smp5.casemodel.SubscriberModel;
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
		parent.setVideoServicePlanAttributes(this);
	}

	public PropHolder video_service_plan_id = new PropHolder(this, "video_service_plan_id", true);
	public PropHolder modify_date = new PropHolder(this, "modify_date", true);
	public PropHolder cableUnit = new PropHolder(this, "cable_unit");
	public PropHolder npvr_enabled = new PropHolder(this, "npvr_enabled");
	public PropHolder npvr_storage_size = new PropHolder(this, "npvr_storage_size");
	public PropHolder webtv_enabled = new PropHolder(this, "webtv_enabled", false);
	public PropHolder has_linked_id = new PropHolder(this, "has_linked_id", false);

	// Type.ASSOC
	public AssociationHolder video_service_defn_has_cas = new AssociationHolder(this, "video_service_defn_has_cas", STBCas.TYPE);

	public VideoServicePlan getParent() {
		return (VideoServicePlan) super.getParent();
	}

}