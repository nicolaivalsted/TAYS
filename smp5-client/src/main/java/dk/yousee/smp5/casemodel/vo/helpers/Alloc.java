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

	public VideoServicePlan VideoServicePlan(BusinessPosition position) {
		VideoServicePlan plan = find.VideoServicePlan(position);
		return plan == null ? add.VideoServicePlan() : plan;
	}

	public VideoComposedService VideoComposedService() {
		VideoComposedService res = find.VideoComposedService();
		if (res == null) {
			res = add.VideoComposedService();
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

	public VideoCPE VideoCPE(String macAdress) {
		VideoCPE res = find.VideoCPE(macAdress);
		return res == null ? add.VideoCPE(macAdress) : res;
	}

	public STBCas STBCas(String macAdress) {
		STBCas res = find.STBCas(macAdress);
		return res == null ? add.STBCas(macAdress) : res;
	}

	public VideoCPEService VideoCPEService() {
		VideoCPEService res = find.VideoCPEService();
		return res == null ? add.VideoCPEService() : res;
	}
}
