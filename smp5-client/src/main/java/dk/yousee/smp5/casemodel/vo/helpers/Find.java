/**
 * 
 */
package dk.yousee.smp5.casemodel.vo.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.base.SampSub;
import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
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
import dk.yousee.smp5.order.model.OrderDataType;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 13:37:25
 */
public class Find {
	private static final Logger logger = Logger.getLogger(Find.class);

	private List<BasicUnit> serviceLevelUnit;
	private Key key;

	public Find(SubscriberModel model, List<BasicUnit> serviceLevelUnit) {
		this.serviceLevelUnit = serviceLevelUnit;
		this.key = model.key();
	}

	BasicUnit find(OrderDataType type) {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(type)) {
				return plan;
			}
		}
		return null;
	}

	BasicUnit find(OrderDataType type, String externalKey) {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(type) && plan.getExternalKey().equals(externalKey)) {
				return plan;
			}
		}
		return null;
	}

	/**
	 * @return the subscribers Contact information
	 */
	public SampSub SampSub() {
		return (SampSub) find(SampSub.TYPE);
	}

	/**
	 * @return the subscribers Contact information
	 */
	public SubContactSpec SubContactSpec() {
		return (SubContactSpec) find(SubContactSpec.TYPE);
	}

	/**
	 * @return the subscribes Address, null if not exists
	 */
	public SubAddressSpec SubAddressSpec() {
		return (SubAddressSpec) find(SubAddressSpec.TYPE);
	}

	// ========= OTT =========
	/**
	 * @return the OTTService the subscriber has
	 */
	public List<OTTService> OTTService() {
		List<OTTService> res = new ArrayList<OTTService>();
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(OTTService.TYPE)) {
				res.add((OTTService) plan);
			}
		}
		return res;
	}

	/**
	 * @param position
	 *            identifier for specific instance of the service plan
	 * @return instance if it exists
	 */
	public OTTService OTTService(BusinessPosition position) {
		List<OTTService> plans = OTTService();
		for (OTTService plan : plans) {
			for (OTTSubscription ottSubscription : plan.getOttSubscriptions()) {
				if (position.equals(ottSubscription.getPosition())) {
					return plan;
				}
			}
		}
		return null;
	}

	public OTTSubscription OTTSubscription(BusinessPosition position, String rateCode) {
		OTTService parent = OTTService(position);
		if (parent == null) {
			return null;
		} else {
			for (OTTSubscription ottSubscription : parent.getOttSubscriptions()) {
				if (position.equals(ottSubscription.getPosition()) && ottSubscription.rate_code.getValue().equals(rateCode)) {
					return ottSubscription;
				}
			}
		}
		return null;
	}

	/**
	 * @param parentKey
	 *            to the CableBBService
	 * @return instance list if it exists
	 */
	public List<OTTSubscription> OTTSubscription(String parentKey) {
		OTTService parent = OTTService(parentKey);
		if (parent == null)
			return null;
		return parent.getOttSubscriptions();
	}

	/**
	 * @param externalKey
	 *            the key composed from modem id
	 * @return instance if it exists
	 */
	protected OTTService OTTService(String externalKey) {
		return (OTTService) find(OTTService.TYPE, externalKey);
	}

	// ========= END OTT =========

	// ========= Video =========

	/**
	 * @return the VideoService the subscriber has
	 */
	public List<VideoComposedService> VideoComposedService() {
		List<VideoComposedService> res = new ArrayList<VideoComposedService>();
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(VideoComposedService.TYPE)) {
				res.add((VideoComposedService) plan);
			}
		}
		return res;
	}

	/**
	 * @param externalKey
	 *            the key composed from modem id
	 * @return instance if it exists
	 */
	protected VideoComposedService VideoComposedService(String externalKey) {
		return (VideoComposedService) find(VideoComposedService.TYPE, externalKey);
	}

	/**
	 * @param position
	 *            identifier for specific instance of the service plan
	 * @return instance if it exists
	 */
	public VideoComposedService VideoComposedService(BusinessPosition position) {
		List<VideoComposedService> plans = VideoComposedService();
		for (VideoComposedService plan : plans) {
			for (VideoServicePlan videoServicePlan : plan.getVideoServicePlans()) {
				if (position.equals(videoServicePlan.getVideoServicePlanAttributes().getPosition())) {
					return plan;
				}
			}
		}
		return null;
	}

	public VideoServicePlan VideoServicePlan(BusinessPosition position) {
		VideoComposedService parent = VideoComposedService(position);
		if (parent == null) {
			return null;
		} else {
			for (VideoServicePlan videoServicePlan : parent.getVideoServicePlans()) {
				if (position.equals(videoServicePlan.getVideoServicePlanAttributes().getPosition())) {
					return videoServicePlan;
				}
			}
		}
		return null;
	}

	public VideoEvent VideoEvent(BusinessPosition position) {
		VideoServicePlan parent = VideoServicePlan(position);
		if (parent == null) {
			return null;
		} else {
			for (VideoEvent videoEvent : parent.getVideoEvents()) {
				if (position.equals(videoEvent.video_entitlement_id.getValue())) {
					return videoEvent;
				}
			}
		}
		return null;
	}

	public VideoServicePlanAttributes VideoServicePlanAttributes(BusinessPosition position) {
		VideoServicePlan parent = VideoServicePlan(position);
		if (parent == null) {
			return null;
		}
		return parent.getVideoServicePlanAttributes();
	}

	public VideoSubscription VideoSubscription(BusinessPosition position) {
		VideoServicePlan parent = VideoServicePlan(position);
		if (parent == null) {
			return null;
		} else {
			for (VideoSubscription videoSubscription : parent.getVideoSubscriptions()) {
				if (position.equals(videoSubscription.video_entitlement_id.getValue())) {
					return videoSubscription;
				}
			}
		}
		return null;
	}

	// ========= END OTT =========

	// ========= Video CPE =========

	/**
	 * @return the VideoCPEService the subscriber has
	 */
	public List<VideoCPEService> VideoCPEService() {
		List<VideoCPEService> res = new ArrayList<VideoCPEService>();
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(VideoCPEService.TYPE)) {
				res.add((VideoCPEService) plan);
			}
		}
		return res;
	}

	/**
	 * @param position
	 *            identifier for specific instance of the service plan
	 * @return instance if it exists
	 */
	public VideoCPEService VideoCPEService(BusinessPosition position) {
		List<VideoCPEService> plans = VideoCPEService();
		for (VideoCPEService plan : plans) {
			for (VideoCPE videoCPE : plan.getVideoCPEs()) {
				if (position.equals(videoCPE.macAddress.getValue())) {
					return plan;
				}
			}
		}
		return null;
	}

	public VideoCPE VideoCPE(BusinessPosition position) {
		VideoCPEService parent = VideoCPEService(position);
		if (parent == null) {
			return null;
		} else {
			for (VideoCPE videoCPE : parent.getVideoCPEs()) {
				if (position.equals(videoCPE.macAddress.getValue())) {
					return videoCPE;
				}
			}
		}
		return null;
	}

	public STBCas STBCas(BusinessPosition businessPosition) {
		VideoCPE parent = VideoCPE(businessPosition);
		if (parent == null) {
			return null;
		} else {
			return parent.getStbCAS();
		}
	}

	// ========= END Video CPE =========

}
