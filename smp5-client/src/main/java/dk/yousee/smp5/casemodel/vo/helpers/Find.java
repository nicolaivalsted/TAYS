package dk.yousee.smp5.casemodel.vo.helpers;

import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.base.SampSub;
import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp5.casemodel.vo.mail.ForeningsMailService;
import dk.yousee.smp5.casemodel.vo.mail.Mail;
import dk.yousee.smp5.casemodel.vo.ott.OTTService;
import dk.yousee.smp5.casemodel.vo.ott.OTTSubscription;
import dk.yousee.smp5.casemodel.vo.sikpakke.Sikkerhedspakke;
import dk.yousee.smp5.casemodel.vo.sikpakke.SikkerhedspakkeService;
import dk.yousee.smp5.casemodel.vo.smartcard.SmartCard;
import dk.yousee.smp5.casemodel.vo.smartcard.SmartCardService;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPE;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPEService;
import dk.yousee.smp5.casemodel.vo.tdcmail.TdcMail;
import dk.yousee.smp5.casemodel.vo.tdcmail.TdcMailService;
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

	public OTTSubscription OTTSubscription(String sik) {
		OTTService parent = OTTService();
		if (parent == null) {
			return null;
		} else {
			for (OTTSubscription ottSubscription : parent.getOttSubscriptions()) {
				if (ottSubscription.sik.getValue().equalsIgnoreCase(sik)) {
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

	public List<VideoSubscription> VideoSubscriptionBP(String id) {
		VideoServicePlan parent = VideoServicePlan();
		List<VideoSubscription> subsList = new ArrayList<VideoSubscription>();
		if (parent == null) {
			return null;
		} else {
			String idTemp = "-" + id + "-";
			for (VideoSubscription videoSubscription : parent.getVideoSubscriptions()) {
				if (videoSubscription.video_entitlement_id.getValue().contains(idTemp)) {
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

	public VideoSubscription VideoSubscription(String entitlementId) {
		VideoServicePlan parent = VideoServicePlan();
		if (parent == null) {
			return null;
		} else {
			for (VideoSubscription videoSubscription : parent.getVideoSubscriptions()) {
				if (entitlementId.equalsIgnoreCase(videoSubscription.video_entitlement_id.getValue())) {
					return videoSubscription;
				}
			}
		}
		return null;
	}

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

	public VideoCPE VideoCPE(String id) {
		VideoCPEService parent = VideoCPEService();
		if (parent == null) {
			return null;
		} else {
			for (VideoCPE videoCPE : parent.getVideoCPEs()) {
				if (id.equalsIgnoreCase(videoCPE.getStbCAS().sik.getValue())) {
					return videoCPE;
				}
			}
		}
		return null;
	}

	public STBCas STBCas(String id) {
		VideoCPE parent = VideoCPE(id);
		if (parent == null) {
			return null;
		} else {
			return parent.getStbCAS();
		}
	}

	/**
	 * @return the Smartcard Composed Service
	 */
	public SmartCardService SmartCardService() {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(SmartCardService.TYPE)) {
				return (SmartCardService) plan;
			}
		}
		return null;
	}

	public SmartCard SmartCard(String sik) {
		SmartCardService parent = SmartCardService();
		if (parent == null) {
			return null;
		} else {
			for (SmartCard smartCard : parent.getSmartCards()) {
				if (smartCard.sik.getValue().equalsIgnoreCase(sik)) {
					return smartCard;
				}
			}
			return null;
		}
	}

	public List<ForeningsMailService> ForeningsMailService() {
		List<ForeningsMailService> res = new ArrayList<ForeningsMailService>();
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(ForeningsMailService.TYPE)) {
				res.add((ForeningsMailService) plan);
			}
		}
		return res;
	}

	/**
	 * @param sik
	 *            identifier for specific instance of the service plan
	 * @return instance if it exists
	 */
	public ForeningsMailService ForeningsMailService(String sik) {
		List<ForeningsMailService> plans = ForeningsMailService();
		for (dk.yousee.smp5.casemodel.vo.mail.ForeningsMailService plan : plans) {
			if (sik.equals(plan.getMail().sik.getValue())) {
				return plan;
			}
		}
		return null;
	}

	/**
	 * @param sik
	 *            to service
	 * @return new instance
	 */
	public Mail ForeningsMail(String sik) {
		ForeningsMailService parent = ForeningsMailService(sik);
		return parent == null ? null : parent.getMail();
	}

	public List<SikkerhedspakkeService> SikkerhedspakkeService() {
		List<SikkerhedspakkeService> res = new ArrayList<SikkerhedspakkeService>();
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(SikkerhedspakkeService.TYPE)) {
				res.add((SikkerhedspakkeService) plan);
			}
		}
		return res;
	}

	/**
	 * @param position
	 *            identifier for specific instance of the service plan
	 * @return instance if it exists
	 */
	public SikkerhedspakkeService SikkerhedspakkeService(String sik) {
		if (sik == null || sik.equals("")) {
			return null;
		}
		List<SikkerhedspakkeService> plans = SikkerhedspakkeService();
		for (SikkerhedspakkeService plan : plans) {
			if (sik.equals(plan.getSikkerhedspakke().sik.getValue())) {
				return plan;
			}
		}
		return null;
	}

	/**
	 *
	 * @param position
	 *            to service
	 * @return new instance
	 */
	public Sikkerhedspakke Sikkerhedspakke(String sik) {
		SikkerhedspakkeService parent = SikkerhedspakkeService(sik);
		return parent == null ? null : parent.getSikkerhedspakke();
	}

	public List<TdcMailService> tdcMailServices() {
		List<TdcMailService> res = new ArrayList<TdcMailService>();
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(TdcMailService.TYPE)) {
				res.add((TdcMailService) plan);
			}
		}

		return res;
	}

	public TdcMailService tdcMailService(String sik) {
		List<TdcMailService> plans = tdcMailServices();
		for (TdcMailService plan : plans) {
			if (sik.equals(plan.getSik())) {
				return plan;
			}
		}
		return null;
	}

	public TdcMail tdcMail(String sik) {
		TdcMailService parent = tdcMailService(sik);
		return parent != null ? parent.getTdcMail() : null;
	}

}
