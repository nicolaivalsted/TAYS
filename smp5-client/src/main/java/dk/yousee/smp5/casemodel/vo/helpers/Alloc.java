package dk.yousee.smp5.casemodel.vo.helpers;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.ott.OTTService;
import dk.yousee.smp5.casemodel.vo.ott.OTTSubscription;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPE;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPEService;
import dk.yousee.smp5.casemodel.vo.video.VideoComposedService;
import dk.yousee.smp5.casemodel.vo.video.VideoEvent;
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

	public OTTSubscription OTTSubscription(BusinessPosition position, String rateCode) {
		OTTSubscription sub = find.OTTSubscription(position, rateCode);
		return sub == null ? add.OTTSubscription(position) : sub;
	}

	public OTTService OTTService(BusinessPosition position) {
		OTTService res = find.OTTService(position);
		if (res == null) {
			res = add.OTTService(position);
		}
		return res;
	}

	public VideoServicePlan VideoServicePlan(BusinessPosition position) {
		VideoServicePlan plan = find.VideoServicePlan(position);
		return plan == null ? add.VideoServicePlan(position) : plan;
	}

	public VideoComposedService VideoComposedService(BusinessPosition position) {
		VideoComposedService res = find.VideoComposedService(position);
		if (res == null) {
			res = add.VideoComposedService(position);
		}
		return res;
	}

	public VideoEvent VideoEvent(BusinessPosition position) {
		VideoEvent videoEvent = find.VideoEvent(position);
		return videoEvent == null ? add.VideoEvent(position) : videoEvent;
	}

	public VideoServicePlanAttributes VideoServicePlanAttributes(BusinessPosition position) {
		VideoServicePlanAttributes res = find.VideoServicePlanAttributes(position);
		return res == null ? add.VideoServicePlanAttributes(position) : res;
	}

	public VideoSubscription VideoSubscription(BusinessPosition position) {
		VideoSubscription res = find.VideoSubscription(position);
		return res == null ? add.VideoSubscription(position) : res;
	}

	public VideoCPE VideoCPE(BusinessPosition businessPosition) {
		VideoCPE res = find.VideoCPE(businessPosition);
		return res == null ? add.VideoCPE(businessPosition) : res;
	}

	public STBCas STBCas(BusinessPosition businessPosition) {
		STBCas res = find.STBCas(businessPosition);
		return res == null ? add.STBCas(businessPosition) : res;
	}

	public VideoCPEService VideoCPEService(BusinessPosition businessPosition) {
		VideoCPEService res = find.VideoCPEService(businessPosition);
		if (res == null) {
			res = add.VideoCPEService(businessPosition);
		}
		return res;
	}
}
