package dk.yousee.smp5.casemodel.vo.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.base.SampSub;
import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp5.casemodel.vo.ott.OTTService;
import dk.yousee.smp5.casemodel.vo.ott.OTTSubscription;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPE;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPEService;
import dk.yousee.smp5.casemodel.vo.video.VideoComposedService;
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
	public OTTService OTTService() {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(OTTService.TYPE)) {
				return (OTTService) plan;
			}
		}
		return null;
	}

	public OTTSubscription OTTSubscription(String entitlement) {
		OTTService parent = OTTService();
		if (parent == null) {
			return null;
		} else {
			for (OTTSubscription ottSubscription : parent.getOttSubscriptions()) {
				if (ottSubscription.ott_entitlement_id.getValue().equals(entitlement)) {
					return ottSubscription;
				}
			}
		}
		return null;
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
	public VideoComposedService VideoComposedService() {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(VideoComposedService.TYPE)) {
				return (VideoComposedService) plan;
			}
		}
		return null;
	}

	/**
	 * @param externalKey
	 *            the key composed from modem id
	 * @return instance if it exists
	 */
	protected VideoComposedService VideoComposedService(String externalKey) {
		return (VideoComposedService) find(VideoComposedService.TYPE, externalKey);
	}

	public VideoServicePlan VideoServicePlan() {
		VideoComposedService parent = VideoComposedService();
		if (parent == null) {
			return null;
		} else {
			return parent.getVideoServicePlan();
		}
	}

	public VideoServicePlanAttributes VideoServicePlanAttributes() {
		VideoServicePlan parent = VideoServicePlan();
		if (parent == null) {
			return null;
		}
		return parent.getVideoServicePlanAttributes();
	}

	public List<VideoSubscription> VideoSubscription(String id) {
		VideoServicePlan parent = VideoServicePlan();
		List<VideoSubscription> subsList = new ArrayList<VideoSubscription>();
		if (parent == null) {
			return null;
		} else {
			for (VideoSubscription videoSubscription : parent.getVideoSubscriptions()) {
				if (id.equals(videoSubscription.video_entitlement_id.getValue())) {
					subsList.add(videoSubscription);
				}
			}
			return subsList;
		}
	}

	public List<VideoSubscription> VideoSubscription() {
		VideoServicePlan parent = VideoServicePlan();
		if (parent == null) {
			return null;
		} else {

			return parent.getVideoSubscriptions();
		}
	}

	public VideoSubscription VideoSubscription(String servicePlanId, String entitlementId) {
		VideoServicePlan parent = VideoServicePlan();
		if (parent == null) {
			return null;
		} else {
			for (VideoSubscription videoSubscription : parent.getVideoSubscriptions()) {
				if (entitlementId.equals(videoSubscription.video_entitlement_id.getValue())) {
					return videoSubscription;
				}
			}
		}
		return null;
	}

	// ========= END VIDEO =========

	// ========= Video CPE =========

	/**
	 * @return the VideoCPEService the subscriber has
	 */
	public VideoCPEService VideoCPEService() {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(VideoCPEService.TYPE)) {
				return (VideoCPEService) plan;
			}
		}
		return null;
	}

	public VideoCPE VideoCPE(String serialNumber) {
		VideoCPEService parent = VideoCPEService();
		if (parent == null) {
			return null;
		} else {
			for (VideoCPE videoCPE : parent.getVideoCPEs()) {
				if (serialNumber.equals(videoCPE.getStbCAS().serialNumber.getValue())) {
					return videoCPE;
				}
			}
		}
		return null;
	}

	public STBCas STBCas(String serialNumber) {
		VideoCPE parent = VideoCPE(serialNumber);
		if (parent == null) {
			return null;
		} else {
			return parent.getStbCAS();
		}
	}

	// ========= END Video CPE =========

}
