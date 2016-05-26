package dk.yousee.smp5.casemodel.vo.helpers;

import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.ModemId;
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
import dk.yousee.smp5.casemodel.vo.voiceline.DialToneAccess;
import dk.yousee.smp5.casemodel.vo.voiceline.SwitchFeature;
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
	 */
	public VoiceService VoiceService(ModemId modemId) {
		List<VoiceService> services = VoiceService();
		for (VoiceService service : services) {
			if (modemId.equals(service.getModemId())) {
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
	public VoiceService VoiceService(BusinessPosition position) {
		List<VoiceService> services = VoiceService();
		for (VoiceService service : services) {
			if (position.equals(service.getPosition())) {
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
	public DialToneAccess DialToneAccess(BusinessPosition position) {
		VoiceService parent = VoiceService(position);
		if (parent == null)
			return null;
		return parent.getDialToneAccess();
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return instance if it exists
	 */
	public DialToneAccess DialToneAccess(ModemId modemId) {
		VoiceService parent = VoiceService(modemId);
		if (parent == null)
			return null;
		return parent.getDialToneAccess();
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return instance list if it exists
	 */
	public List<SwitchFeature> SwitchFeature(BusinessPosition position) {
		VoiceService parent = VoiceService(position);
		if (parent == null)
			return null;
		return parent.getSwitchFeatureList();
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return instance if it exists
	 */
	public VoiceMail VoiceMail(BusinessPosition position) {
		VoiceService parent = VoiceService(position);
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

	/**
	 * @param externalKey
	 *            the key composed from modem id
	 * @return instance if it exists
	 */
	protected CableBBService CableBBService(String externalKey) {
		return (CableBBService) find(CableBBService.TYPE, externalKey);
	}

	/**
	 * @param modemId
	 *            the key composed from modem id
	 * @return instance if it exists
	 */
	public CableBBService CableBBService(ModemId modemId) {

		List<CableBBService> services = CableBBService();
		for (CableBBService service : services) {
			InetAccess access = service.getInetAccess();
			if (access == null) {
				return CableBBService(key.CableBBService(modemId));
			} else {
				ModemId one = access.getModemId();
				if (modemId.equals(one))
					return service;
			}
		}
		return null;
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return instance if it exists
	 */
	public InetAccess InetAccess(ModemId modemId) {
		return InetAccess(key.CableBBService(modemId));
	}

	/**
	 * @param parentKey
	 *            to the CableBBService
	 * @return instance if it exists
	 */
	protected InetAccess InetAccess(String parentKey) {
		CableBBService parent = CableBBService(parentKey);
		if (parent == null)
			return null;
		return parent.getInetAccess();
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return instance if it exists
	 */
	public SMPStaticIP SMPStaticIP(ModemId modemId) {
		CableBBService parent = CableBBService(modemId);
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

	/**
	 * @param cmOwnership
	 *            key to the composed service (currently modem id)
	 * @return instance if it exists
	 */
	public MTAService MTAService(ModemId cmOwnership) {
		List<MTAService> services = MTAService();
		for (MTAService service : services) {
			if (cmOwnership.equals(service.getCmOwnership())) {
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
	public HsdAccess HsdAccess(ModemId cmOwnership) {
		MTAService parent = MTAService(cmOwnership);
		if (parent == null)
			return null;
		return parent.getHsdAccess();
	}

	/**
	 * @param cmOwnership
	 *            key to the composed service (currently modem id)
	 * @return instance if it exists
	 */
	public DeviceControl DeviceControl(ModemId cmOwnership) {
		MTAService parent = MTAService(cmOwnership);
		if (parent == null)
			return null;
		return parent.getDeviceControl();
	}

	/**
	 * @param cmOwnership
	 *            key to the composed service (currently modem id)
	 * @return instance if it exists
	 */
	public VoipAccess VoipAccess(ModemId cmOwnership) {
		MTAService parent = MTAService(cmOwnership);
		if (parent == null)
			return null;
		return parent.getVoipAccess();
	}

	/**
	 * @param modemId
	 *            to modem
	 * @return instance if it exists
	 */
	public StdCpe StdCpe(ModemId modemId) {
		MTAService service = MTAService(modemId);
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
	public AddnCpe AddnCpe(ModemId modemId) {
		MTAService parent = MTAService(modemId);
		if (parent == null)
			return null;
		return parent.getAddnCpe();
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @param childKey
	 *            what is this ???
	 * @return instance list if it exists
	 * @deprecated - it is next to unusable. Additional CPE's are identificed by
	 *             different MAC addresses. Therefore the correct behaviour is
	 *             to collect all AddnCpe's for a modem and for each of these to
	 *             find the correct MAC address and run from there.
	 */
	public AddnCpe AddnCpe(ModemId modemId, String childKey) {
		return AddnCpeAndChildKey(modemId, childKey);
	}

	/**
	 * @param parentKey
	 *            to the CableBBService
	 * @param childKey
	 *            what is this ???
	 * @return instance if it exists
	 */
	public AddnCpe AddnCpeAndChildKey(ModemId modemId, String childKey) {
		MTAService parent = MTAService(modemId);
		if (parent == null) {
			return null;
		} else {
			AddnCpe addnCpe = parent.getAddnCpe();
			if (addnCpe != null) {
				if (addnCpe.getExternalKey().equalsIgnoreCase(childKey)) {
					return addnCpe;
				}
			}
		}
		return null;
	}

	public AddnCpe theAddnCpe(ModemId modemId) {
		MTAService parent = MTAService(modemId);
		if (parent == null)
			return null;
		return parent.getAddnCpe();
	}

}
