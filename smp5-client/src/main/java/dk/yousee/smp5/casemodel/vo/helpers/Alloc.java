package dk.yousee.smp5.casemodel.vo.helpers;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.ott.OTTService;
import dk.yousee.smp5.casemodel.vo.ott.OTTSubscription;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPE;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPEService;
import dk.yousee.smp5.casemodel.vo.video.VideoComposedService;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlan;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.casemodel.vo.video.VideoSubscription;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 13:38:05
 */
public class Alloc {
	private Find find;
	private Add add;

	public Alloc(SubscriberModel model) {
		find = model.find();
		add = model.add();
	}

	public OTTSubscription OTTSubscription(String rateCode) {
		OTTSubscription sub = find.OTTSubscription(rateCode);
		return sub == null ? add.OTTSubscription() : sub;
	}

	public OTTService OTTService() {
		OTTService res = find.OTTService();
		if (res == null) {
			res = add.OTTService();
		}
		return res;
	}

	public VideoServicePlan VideoServicePlan(String servicePlanId) {
		VideoServicePlan plan = find.VideoServicePlan(servicePlanId);
		return plan == null ? add.VideoServicePlan() : plan;
	}

	public VideoComposedService VideoComposedService() {
		VideoComposedService res = find.VideoComposedService();
		if (res == null) {
			res = add.VideoComposedService();
		}
		return res;
	}

	public VideoServicePlanAttributes VideoServicePlanAttributes(String servicePlanId) {
		VideoServicePlanAttributes res = find.VideoServicePlanAttributes(servicePlanId);
		return res == null ? add.VideoServicePlanAttributes(servicePlanId) : res;
	}

	public VideoSubscription VideoSubscription(String servicePlanId) {
		VideoSubscription res = find.VideoSubscription(servicePlanId);
		return res == null ? add.VideoSubscription(servicePlanId) : res;
	}

	public VideoCPE VideoCPE(String serialNumber) {
		VideoCPE res = find.VideoCPE(serialNumber);
		return res == null ? add.VideoCPE(serialNumber) : res;
	}

	public STBCas STBCas(String serialNumber) {
		STBCas res = find.STBCas(serialNumber);
		return res == null ? add.STBCas(serialNumber) : res;
	}

	public VideoCPEService VideoCPEService() {
		VideoCPEService res = find.VideoCPEService();
		return res == null ? add.VideoCPEService() : res;
	}
}
