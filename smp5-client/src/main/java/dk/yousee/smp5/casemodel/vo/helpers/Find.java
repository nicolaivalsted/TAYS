package dk.yousee.smp5.casemodel.vo.helpers;

import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.PhoneNumber;
import dk.yousee.smp5.casemodel.vo.base.SampSub;
import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp5.casemodel.vo.cablebb.CableBBService;
import dk.yousee.smp5.casemodel.vo.cablebb.InetAccess;
import dk.yousee.smp5.casemodel.vo.cablebb.SMPStaticIP;
import dk.yousee.smp5.casemodel.vo.emta.AddnCpe;
import dk.yousee.smp5.casemodel.vo.emta.DeviceControl;
import dk.yousee.smp5.casemodel.vo.emta.HsdAccess;
import dk.yousee.smp5.casemodel.vo.emta.MTAService;
import dk.yousee.smp5.casemodel.vo.emta.StdCpe;
import dk.yousee.smp5.casemodel.vo.emta.VoipAccess;
import dk.yousee.smp5.casemodel.vo.fordel.FordelComposed;
import dk.yousee.smp5.casemodel.vo.fordel.FordelSubscription;
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
import dk.yousee.smp5.casemodel.vo.video.AppSubscription;
import dk.yousee.smp5.casemodel.vo.video.VideoComposedService;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlan;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.casemodel.vo.video.VideoSubscription;
import dk.yousee.smp5.casemodel.vo.voiceline.DialToneAccess;
import dk.yousee.smp5.casemodel.vo.voiceline.MailBox;
import dk.yousee.smp5.casemodel.vo.voiceline.VoiceMail;
import dk.yousee.smp5.casemodel.vo.voiceline.VoiceService;
import dk.yousee.smp5.order.model.OrderDataType;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 13:37:25
 */
public class Find {

	private List<BasicUnit> serviceLevelUnit;

	public Find(SubscriberModel model, List<BasicUnit> serviceLevelUnit) {
		this.serviceLevelUnit = serviceLevelUnit;
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

	public List<AppSubscription> AppSubscription() {
		VideoServicePlan parent = VideoServicePlan();
		if (parent == null) {
			return null;
		} else {
			return parent.getAppSubscriptions();
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

	public AppSubscription AppSubscription(String sik) {
		VideoServicePlan parent = VideoServicePlan();
		if (parent == null) {
			return null;
		} else {
			for (AppSubscription appSubscription : parent.getAppSubscriptions()) {
				if (sik.equalsIgnoreCase(appSubscription.sik.getValue())) {
					return appSubscription;
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

	public STBCas findFirstSTB() {
		VideoCPEService parent = VideoCPEService();
		if (parent == null) {
			return null;
		} else {
			for (VideoCPE videoCPE : parent.getVideoCPEs()) {
				return videoCPE.getStbCAS();
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

	public SikkerhedspakkeService findFirstSikkerhedspakkeService() {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(SikkerhedspakkeService.TYPE)) {
				return (SikkerhedspakkeService) plan;
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

	public List<TdcMailService> TdcMailService() {
		List<TdcMailService> res = new ArrayList<TdcMailService>();
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(TdcMailService.TYPE)) {
				res.add((TdcMailService) plan);
			}
		}

		return res;
	}

	public TdcMailService TdcMailService(String sik) {
		List<TdcMailService> plans = TdcMailService();
		for (TdcMailService plan : plans) {
			if (sik.equals(plan.getSik())) {
				return plan;
			}
		}
		return null;
	}

	public TdcMail tdcMail(String sik) {
		TdcMailService parent = TdcMailService(sik);
		return parent != null ? parent.getTdcMail() : null;
	}

	/**
	 * @return the VoiceService the subscriber has
	 */
	public List<VoiceService> VoiceService() {
		List<VoiceService> res = new ArrayList<VoiceService>();
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(VoiceService.TYPE)) {
				res.add((VoiceService) plan);
			}
		}
		return res;
	}

	/**
	 * @param phoneNumber
	 *            to the modem
	 * @return instance if it exists
	 */
	public VoiceService VoiceService(PhoneNumber phoneNumber) {
		List<VoiceService> services = VoiceService();
		for (VoiceService service : services) {
			if (phoneNumber.equals(service.getPhoneNumber())) {
				return service;
			}
		}
		return null;
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return instance if it exists
	 * 
	 */
	public VoiceService VoiceService(String sik) {
		List<VoiceService> services = VoiceService();
		for (VoiceService service : services) {
			if (sik.equalsIgnoreCase(service.getSik())) {
				return service;
			}
		}

		return null;
	}

	/**
	 * @param BusinessPosition
	 *            to the modem
	 * @return instance if it exists
	 */
	public DialToneAccess DialToneAccess(String sik) {
		VoiceService parent = VoiceService(sik);
		if (parent == null)
			return null;
		return parent.getDialToneAccess();
	}

	public DialToneAccess findFirstVoiceDial() {
		List<VoiceService> voices = VoiceService();
		for (VoiceService voice : voices) {
			return voice.getDialToneAccess();
		}
		return null;
	}

	public VoiceService findFirstVoice() {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(VoiceService.TYPE)) {
				VoiceService voice = (VoiceService) plan;
				return voice;
			}
		}
		return null;
	}

	public ForeningsMailService findFirstForeningsMailService() {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(ForeningsMailService.TYPE)) {
				return (ForeningsMailService) plan;
			}
		}
		return null;
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return instance if it exists
	 */
	public MailBox MailBox(String sik) {
		VoiceMail parent = VoiceMail(sik);
		if (parent == null)
			return null;
		return parent.getMailBox();
	}

	public MailBox MailBox() {
		VoiceMail parent = VoiceMail2();
		if (parent == null)
			return null;
		return parent.getMailBox();
	}

	public VoiceMail VoiceMail(String sik) {
		VoiceService parent = VoiceService(sik);
		if (parent == null)
			return null;
		return parent.getVoiceMail();
	}

	public VoiceMail VoiceMail2() {
		VoiceService parent = findFirstVoice();
		if (parent == null)
			return null;
		return parent.getVoiceMail();
	}

	/**
	 * @return the CableBBServices the subscriber has
	 */
	public List<CableBBService> CableBBService() {
		List<CableBBService> res = new ArrayList<CableBBService>();
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(CableBBService.TYPE)) {
				res.add((CableBBService) plan);
			}
		}
		return res;
	}

	public CableBBService CableBBServiceSik(String sik) {
		List<CableBBService> services = CableBBService();
		for (CableBBService service : services) {
			if (sik.equals(service.getSik())) {
				return service;
			}
		}
		return null;
	}

	/**
	 * First Cable Modem matching the activation code
	 *
	 * @param modemActivationCode
	 *            code to search for (case sensitive etc)
	 * @return instance if it exists
	 */
	public CableBBService firstCableBBService(String modemActivationCode) {
		if (modemActivationCode == null)
			throw new IllegalArgumentException("modemActivationCode is mandatory in query");
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(CableBBService.TYPE)) {
				CableBBService bb = (CableBBService) plan;
				if (modemActivationCode.equals(bb.getInetAccess().getModemActivationCode())) {
					return bb;
				}
			}
		}
		return null;
	}

	public CableBBService firstOneCableBBService() {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(CableBBService.TYPE)) {
				return (CableBBService) plan;
			}
		}
		return null;
	}

	public TdcMailService findFirstTdcMail() {
		List<TdcMailService> mailList = TdcMailService();
		for (TdcMailService mail : mailList) {
			return mail;
		}
		return null;
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return instance if it exists
	 */
	public InetAccess InetAccess(String sik) {
		CableBBService parent = CableBBServiceSik(sik);
		if (parent == null) {
			return null;
		}
		return parent.getInetAccess();
	}

	public InetAccess findFirstInternet() {
		List<CableBBService> bbs = CableBBService();
		for (CableBBService bb : bbs) {
			return bb.getInetAccess();
		}
		return null;
	}

	public DeviceControl findFirstDeviceControl() {
		List<MTAService> mts = MTAService();
		for (MTAService mt : mts) {
			return mt.getDeviceControl();
		}
		return null;
	}

	public VoipAccess findFirsteMta() {
		List<MTAService> mtas = MTAService();
		for (MTAService mta : mtas) {
			if (mta.getVoipAccess() != null) {
				return mta.getVoipAccess();
			}
		}
		return null;
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return instance if it exists
	 */
	public SMPStaticIP SMPStaticIP(String sik) {
		CableBBService parent = CableBBServiceSik(sik);
		if (parent == null)
			return null;
		return parent.getSmpStaticIP();
	}

	public List<MTAService> MTAService() {
		List<MTAService> res = new ArrayList<MTAService>();
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(MTAService.TYPE)) {
				res.add((MTAService) plan);
			}
		}
		return res;
	}

	public MTAService findFirstdMTAService() {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(MTAService.TYPE)) {
				return (MTAService) plan;
			}
		}
		return null;
	}

	/**
	 * @param cmOwnership
	 *            key to the composed service (currently modem id)
	 * @return instance if it exists
	 */
	public MTAService MTAService(String sik) {
		List<MTAService> services = MTAService();
		for (MTAService service : services) {
			if (sik.equals(service.getSik())) {
				return service;
			}
		}
		return null;
	}

	/**
	 * @param cmOwnership
	 *            key to the composed service (currently modem id)
	 * @return instance if it exists
	 */
	public HsdAccess HsdAccess(String sik) {
		MTAService parent = MTAService(sik);
		if (parent == null)
			return null;
		return parent.getHsdAccess();
	}

	/**
	 * @param cmOwnership
	 *            key to the composed service (currently modem id)
	 * @return instance if it exists
	 */
	public DeviceControl DeviceControl(String sik) {
		MTAService parent = MTAService(sik);
		if (parent == null)
			return null;
		return parent.getDeviceControl();
	}

	/**
	 * @param cmOwnership
	 *            key to the composed service (currently modem id)
	 * @return instance if it exists
	 */
	public VoipAccess VoipAccess(String sik) {
		MTAService parent = MTAService(sik);
		if (parent == null)
			return null;
		return parent.getVoipAccess();
	}

	/**
	 * @param modemId
	 *            to modem
	 * @return instance if it exists
	 */
	public StdCpe StdCpe(String sik) {
		MTAService service = MTAService(sik);
		if (service != null) {
			return service.getStdCpe();
		} else {
			return null;
		}
	}

	/**
	 * @param parentKey
	 *            to the CableBBService
	 * @return instance list if it exists
	 */
	public AddnCpe AddnCpe(String sik) {
		MTAService parent = MTAService(sik);
		if (parent == null)
			return null;
		return parent.getAddnCpe();
	}

	public AddnCpe theAddnCpe(String sik) {
		MTAService parent = MTAService(sik);
		if (parent == null)
			return null;
		return parent.getAddnCpe();
	}

	public FordelComposed FordelComposed() {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(FordelComposed.TYPE)) {
				return (FordelComposed) plan;
			}
		}
		return null;
	}

	public FordelSubscription FordelSubscription(String sik) {
		FordelComposed parent = FordelComposed();
		for (FordelSubscription fordel : parent.getFordelSubscriptions()) {
			if (sik.equals(fordel.sik.getValue())) {
				return fordel;
			}
		}
		return null;
	}

}
