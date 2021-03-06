package dk.yousee.smp5.casemodel.vo.helpers;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.cablebb.CableBBService;
import dk.yousee.smp5.casemodel.vo.cablebb.InetAccess;
import dk.yousee.smp5.casemodel.vo.cablebb.SMPStaticIP;
import dk.yousee.smp5.casemodel.vo.emta.AddnCpe;
import dk.yousee.smp5.casemodel.vo.emta.DeviceControl;
import dk.yousee.smp5.casemodel.vo.emta.HsdAccess;
import dk.yousee.smp5.casemodel.vo.emta.MTAService;
import dk.yousee.smp5.casemodel.vo.emta.StdCpe;
import dk.yousee.smp5.casemodel.vo.emta.VoipAccess;
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
import dk.yousee.smp5.casemodel.vo.video.AppSubscription;
import dk.yousee.smp5.casemodel.vo.video.VideoComposedService;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlan;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.casemodel.vo.video.VideoSubscription;
import dk.yousee.smp5.casemodel.vo.voiceline.DialToneAccess;
import dk.yousee.smp5.casemodel.vo.voiceline.MailBox;
import dk.yousee.smp5.casemodel.vo.voiceline.VoiceMail;
import dk.yousee.smp5.casemodel.vo.voiceline.VoiceService;

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

	public OTTSubscription OTTSubscription(String sik) {
		OTTSubscription sub = find.OTTSubscription(sik);
		return sub == null ? add.OTTSubscription() : sub;
	}

	public OTTService OTTService() {
		OTTService res = find.OTTService();
		if (res == null) {
			res = add.OTTService();
		}
		return res;
	}

	public VideoServicePlan VideoServicePlan(String acct) {
		VideoServicePlan plan = find.VideoServicePlan();
		return plan == null ? add.VideoServicePlan(acct) : plan;
	}

	public VideoComposedService VideoComposedService(String acct) {
		VideoComposedService res = find.VideoComposedService();
		if (res == null) {
			res = add.VideoComposedService(acct);
		}
		return res;
	}

	public VideoServicePlanAttributes VideoServicePlanAttributes(String acct) {
		VideoServicePlanAttributes res = find.VideoServicePlanAttributes();
		return res == null ? add.VideoServicePlanAttributes(acct) : res;
	}

	public VideoSubscription VideoSubscription(String entitlementId, String parcos, String acct) {
		VideoSubscription res = find.VideoSubscription(entitlementId);
		return res == null ? add.VideoSubscription(acct) : res;
	}

	public AppSubscription AppSubscription(String sik, String acct) {
		AppSubscription res = find.AppSubscription(sik);
		return res == null ? add.AppSubscription(acct) : res;
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

	public SmartCard SmartCard(String acct, String sik) {
		SmartCard res = find.SmartCard(sik);
		return res == null ? add.SmartCard(acct) : res;
	}

	public SmartCardService SmartCardService(String acct) {
		SmartCardService res = find.SmartCardService();
		return res == null ? add.SmartCardService(acct) : res;
	}

	/**
	 * @param sik
	 *            to service
	 * @return new instance
	 */
	public Mail ForeningsMail(String sik) {
		Mail sub = find.ForeningsMail(sik);
		return sub == null ? add.ForeningsMail(sik) : sub;
	}

	/**
	 * @param position
	 *            to service
	 * @return new instance
	 */
	public Sikkerhedspakke Sikkerhedspakke(String sik) {
		Sikkerhedspakke sub = find.Sikkerhedspakke(sik);
		return sub == null ? add.Sikkerhedspakke() : sub;
	}

	public TdcMail TdcMail(String sik) {
		TdcMail mail = find.tdcMail(sik);
		return mail == null ? add.tdcMail(sik) : mail;
	}

	/**
	 * @param sik
	 *            to modem
	 * @return instance either an existing plan or a new plan ready for fill in
	 *         data
	 */
	public VoiceService VoiceService() {
		return add.VoiceService();
	}

	public VoiceService VoiceService1() {
		VoiceService res = find.findFirstVoice();
		if (res == null) {
			return add.VoiceService();
		}
		return res;
	}

	/**
	 * @param sik
	 *            to modem
	 * @return instance either an existing plan or a new plan ready for fill in
	 *         data
	 */
	public VoiceService VoiceService(String sik) {
		VoiceService res = find.VoiceService(sik);

		if (res == null) {
			res = add.VoiceService();
		}
		return res;
	}

	/**
	 * @param sik
	 *            to modem
	 * @return instance either an existing child-service or a new child-service
	 *         ready for fill in data
	 */
	public DialToneAccess DialToneAccess(String sik) {
		DialToneAccess res = find.DialToneAccess(sik);
		if (res == null) {
			res = add.DialToneAccess();
		}
		return res;
	}

	public DialToneAccess DialToneAccessFirst() {
		DialToneAccess res = find.findFirstVoiceDial();
		if (res == null) {
			res = add.DialToneAccess();
		}
		return res;
	}

	/**
	 * @param sik
	 *            to modem
	 * @return instance either an existing child-service or a new child-service
	 *         ready for fill in data
	 */
	public MailBox MailBox(String sik) {
		MailBox res = find.MailBox(sik);
		if (res == null) {
			res = add.MailBox(sik);
		}
		return res;
	}

	public VoiceMail VoiceMail(String sik) {
		VoiceMail res = find.VoiceMail2();
		if (res == null) {
			res = add.VoiceMail(sik);
		}
		return res;
	}

	/**
	 * @param sik
	 *            to modem
	 * @return instance either an existing plan or a new plan ready for fill in
	 *         data
	 */
	public CableBBService CableBBService(String sik) {
		CableBBService res = find.CableBBServiceSik(sik);
		if (res == null) {
			res = add.CableBBService(sik);
		}
		return res;

	}

	/**
	 * @param sik
	 *            to modem
	 * @return instance either an existing child-service or a new child-service
	 *         ready for fill in data
	 */
	public InetAccess InetAccess(String sik) {
		InetAccess res = find.InetAccess(sik);
		if (res == null) {
			res = add.InetAccess(sik);
		}
		return res;
	}

	/**
	 * @param sik
	 *            to modem
	 * @return instance either an existing child-service or a new child-service
	 *         ready for fill in data
	 */
	public SMPStaticIP SMPStaticIP(String sik) {
		SMPStaticIP res = find.SMPStaticIP(sik);
		if (res == null) {
			return add.SMPStaticIP(sik);
		}
		return res;
	}

	/**
	 * @param cmOwnership
	 *            to modem
	 * @return instance either an existing plan or a new plan ready for fill in
	 *         data
	 */
	public MTAService MTAService(String cmOwnership) {
		MTAService res = find.MTAService(cmOwnership);
		if (res == null) {
			res = add.MTAService(cmOwnership);
		}
		return res;
	}

	/**
	 * @param sik
	 *            to modem
	 * @return instance either an existing child-service or a new child-service
	 *         ready for fill in data
	 */
	public StdCpe StdCpe(String sik) {
		StdCpe res = find.StdCpe(sik);
		if (res == null) {
			res = add.StdCpe(sik);
		}
		return res;
	}

	public AddnCpe AddnCpe(String sik) {
		AddnCpe res = find.AddnCpe(sik);
		if (res == null) {
			res = add.AddnCpe(sik);
		}
		return res;
	}

	/**
	 * @param sik
	 *            to modem
	 * @return instance either an existing child-service or a new child-service
	 *         ready for fill in data
	 */
	public HsdAccess HsdAccess(String sik) {
		HsdAccess res = find.HsdAccess(sik);
		if (res == null) {
			res = add.HsdAccess(sik);
		} else
			res.getDefaultOrderData();
		return res;
	}

	/**
	 * @param sik
	 *            to modem
	 * @return instance either an existing child-service or a new child-service
	 *         ready for fill in data
	 */
	public VoipAccess VoipAccess(String sik) {
		VoipAccess res = find.VoipAccess(sik);
		if (res == null) {
			res = add.VoipAccess(sik);
		}
		return res;
	}

	/**
	 * @param sik
	 *            to modem
	 * @return instance either an existing child-service or a new child-service
	 *         ready for fill in data
	 */
	public DeviceControl DeviceControl(String sik) {
		DeviceControl res = find.DeviceControl(sik);
		if (res == null) {
			res = add.DeviceControl(sik);
		} else
			res.getDefaultOrderData();
		return res;
	}

}
