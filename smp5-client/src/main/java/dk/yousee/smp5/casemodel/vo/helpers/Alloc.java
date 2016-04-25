package dk.yousee.smp5.casemodel.vo.helpers;

import dk.yousee.smp5.casemodel.SubscriberModel;
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

	public TdcMail tdcMail(String sik) {
		TdcMail mail = find.tdcMail(sik);
		return mail == null ? add.tdcMail(sik) : mail;
	}

}
