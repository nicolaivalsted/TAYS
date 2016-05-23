package dk.yousee.smp5.casemodel.vo.helpers;

import java.util.UUID;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp5.casemodel.vo.base.SubSpec;
import dk.yousee.smp5.casemodel.vo.mail.Mail;
import dk.yousee.smp5.casemodel.vo.ott.OTTService;
import dk.yousee.smp5.casemodel.vo.ott.OTTSubscription;
import dk.yousee.smp5.casemodel.vo.sikpakke.Sikkerhedspakke;
import dk.yousee.smp5.casemodel.vo.smartcard.SmartCard;
import dk.yousee.smp5.casemodel.vo.smartcard.SmartCardService;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPE;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPEService;
import dk.yousee.smp5.casemodel.vo.tdcmail.TdcMail;
import dk.yousee.smp5.casemodel.vo.video.VideoComposedService;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlan;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.casemodel.vo.video.VideoSubscription;
import dk.yousee.smp5.casemodel.vo.voiceline.DialToneAccess;
import dk.yousee.smp5.casemodel.vo.voiceline.SwitchFeature;
import dk.yousee.smp5.casemodel.vo.voiceline.VoiceMail;
import dk.yousee.smp5.casemodel.vo.voiceline.VoiceService;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 13:37:03 This Class handles the creation of
 *         service plans
 */
public class Add {
	private SubscriberModel model;
	private Key key;

	/**
	 * @param mode
	 */
	public Add(SubscriberModel model) {
		this.model = model;
		this.key = model.key();
	}

	public SubSpec SubSpec(String externalKey) {
		SubSpec res = new SubSpec(model, externalKey);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	/**
	 * @return the subscribers Contact information
	 */
	public SubContactSpec SubContactSpec() {
		SubContactSpec res = new SubContactSpec(model);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	/**
	 * @return the subscribes Address
	 */
	public SubAddressSpec SubAddressSpec() {
		SubAddressSpec res = new SubAddressSpec(model);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public OTTSubscription OTTSubscription() {
		OTTService parent = model.alloc().OTTService();
		OTTSubscription res = new OTTSubscription(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public OTTService OTTService() {
		OTTService res = new OTTService(model, key.generateUUID());
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public VideoServicePlan VideoServicePlan(String acct) {
		VideoComposedService parent = model.alloc().VideoComposedService(acct);
		VideoServicePlan res = new VideoServicePlan(model, key.geenerateKeyVideo(acct, "video_service_plan"), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public VideoComposedService VideoComposedService(String acct) {
		VideoComposedService res = new VideoComposedService(model, key.geenerateKeyVideo(acct, "video_services_composed"));
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public VideoServicePlanAttributes VideoServicePlanAttributes(String acct) {
		VideoServicePlan parent = model.alloc().VideoServicePlan(acct);
		VideoServicePlanAttributes res = new VideoServicePlanAttributes(model, key.geenerateKeyVideo(acct, "video_access"), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public VideoSubscription VideoSubscription(String acct) {
		VideoServicePlan parent = model.alloc().VideoServicePlan(acct);
		VideoSubscription res = new VideoSubscription(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public VideoCPE VideoCPE(String serialNumber) {
		VideoCPEService parent = model.alloc().VideoCPEService();
		VideoCPE res = new VideoCPE(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public STBCas STBCas(String serialNumber) {
		VideoCPE parent = model.alloc().VideoCPE(serialNumber);
		STBCas res = new STBCas(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public VideoCPEService VideoCPEService() {
		VideoCPEService res = new VideoCPEService(model, key.generateUUID());
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public SmartCardService SmartCardService(String acct) {
		SmartCardService res = new SmartCardService(model, key.generateKeySmartCardComposed(acct));
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public SmartCard SmartCard(String acct) {
		SmartCardService parent = model.alloc().SmartCardService(acct);
		SmartCard res = new SmartCard(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	/**
	 * @param position
	 *            to service
	 * @return new instance
	 */
	public Mail ForeningsMail(String position) {
		return new Mail(model);
	}

	public Sikkerhedspakke Sikkerhedspakke() {
		return new Sikkerhedspakke(model);
	}

	public TdcMail tdcMail(String sik) {
		return new TdcMail(model, sik);
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return new instance
	 */
	public VoiceService VoiceService() {
		VoiceService res = new VoiceService(model, key.VoiceService(UUID.randomUUID().toString()));
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return new instance
	 */
	public DialToneAccess DialToneAccess() {
		VoiceService parent = model.alloc().VoiceService();
		DialToneAccess res = new DialToneAccess(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return new instance
	 */
	public SwitchFeature SwitchFeature(BusinessPosition position, VoiceService parent) {
		SwitchFeature res = new SwitchFeature(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	/**
	 * @param modemId
	 *            to the modem
	 * @return new instance
	 */
	public VoiceMail VoiceMail(BusinessPosition position) {
		VoiceService parent = model.alloc().VoiceService(position);
		VoiceMail res = new VoiceMail(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}
}
