package dk.yousee.smp5.casemodel.vo.stb;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 27/10/2015 Time: 14:34:31
 */
public class VideoCPE extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "video_cpe");

	STBCas stbCAS;

	public VideoCPE(SubscriberModel model, String externalKey, VideoCPEService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.getVideoCPEs().add(this);
	}

	public VideoCPEService getParent() {
		return (VideoCPEService) super.getParent();
	}

	public STBCas getStbCAS() {
		return stbCAS;
	}

	public void setStbCAS(STBCas stbCAS) {
		this.stbCAS = stbCAS;
	}
}
