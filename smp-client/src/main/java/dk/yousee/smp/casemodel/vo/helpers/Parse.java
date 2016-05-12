package dk.yousee.smp.casemodel.vo.helpers;

import org.apache.log4j.Logger;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.base.SampSub;
import dk.yousee.smp.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp.casemodel.vo.cbp.AddnCpe;
import dk.yousee.smp.casemodel.vo.cbp.BSA;
import dk.yousee.smp.casemodel.vo.cbp.CableBBService;
import dk.yousee.smp.casemodel.vo.cbp.InetAccess;
import dk.yousee.smp.casemodel.vo.cbp.SMPEmail;
import dk.yousee.smp.casemodel.vo.cbp.SMPStaticIP;
import dk.yousee.smp.casemodel.vo.cbp.SMPWiFi;
import dk.yousee.smp.casemodel.vo.cbp.StdCpe;
import dk.yousee.smp.casemodel.vo.cpee.CpeComposedService;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
import dk.yousee.smp.casemodel.vo.cpee.VoipAccess;
import dk.yousee.smp.casemodel.vo.cvp.CableVoiceService;
import dk.yousee.smp.casemodel.vo.cvp.DialToneAccess;
import dk.yousee.smp.casemodel.vo.cvp.SwitchFeature;
import dk.yousee.smp.casemodel.vo.cvp.VoiceMail;
import dk.yousee.smp.casemodel.vo.mail.ForeningsMailService;
import dk.yousee.smp.casemodel.vo.mail.Mail;
import dk.yousee.smp.casemodel.vo.mbs.MobileBBService;
import dk.yousee.smp.casemodel.vo.mbs.SMPMobileBroadbandAttributes;
import dk.yousee.smp.casemodel.vo.mbs.SMPMobileBroadbandDEF;
import dk.yousee.smp.casemodel.vo.mbs.SMPSIMCard;
import dk.yousee.smp.casemodel.vo.mofibo.Mofibo;
import dk.yousee.smp.casemodel.vo.mofibo.MofiboService;
import dk.yousee.smp.casemodel.vo.play.Play;
import dk.yousee.smp.casemodel.vo.play.PlayService;
import dk.yousee.smp.casemodel.vo.sikpakke.Sikkerhedspakke;
import dk.yousee.smp.casemodel.vo.sikpakke.SikkerhedspakkeService;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMail;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMailResource;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMailService;
import dk.yousee.smp.order.model.ResponseEntity;

/**
 * Created by IntelliJ IDEA. User: aka Date: Oct 17, 2010 Time: 10:51:30 PM<br/>
 * Helper to parse response from subscribers content
 */
public class Parse {

	private static final Logger logger = Logger.getLogger(Parse.class);
	private SubscriberModel model;

	public Parse(SubscriberModel model) {
		this.model = model;
	}

	// /**
	// * temporary method to change from TestPrefix
	// *
	// * @param ExternalKey the node containing plans
	// */
	// public String convertFrom(String ExternalKey, String test, String real){
	// return ExternalKey.replaceAll(test,real);
	// }

	/**
	 * Load the customer response into SubscriberModel.
	 *
	 * @param top
	 *            the node containing plans
	 */
	public void buildSubscriberModel(ResponseEntity top) {
		if (top == null) {
			return;
		}
		if (top.getLid() != null) {
			model.getSubscriber().setLid(top.getLid());
		}
		for (ResponseEntity plan : top.getEntities()) {
			if (plan.getType().equals(SubContactSpec.TYPE)) {
				new SubContactSpec(model);
			} else if (plan.getType().equals(SubAddressSpec.TYPE)) {
				new SubAddressSpec(model);
			} else if (plan.getType().equals(SampSub.TYPE)) {
				new SampSub(model, plan.getExternalKey());
			} else if (plan.getType().equals(CableBBService.TYPE)) { // cpe_composed_123452045
																		// YouSee:cpe_123452045
				CableBBService cableBBService = new CableBBService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(InetAccess.TYPE)) {
						new InetAccess(model, child.getExternalKey(), cableBBService);
					} else if (child.getType().equals(StdCpe.TYPE)) {
						new StdCpe(model, child.getExternalKey(), cableBBService);
					} else if (child.getType().equals(AddnCpe.TYPE)) {
						new AddnCpe(model, child.getExternalKey(), cableBBService);
					} else if (child.getType().equals(SMPEmail.TYPE)) {
						new SMPEmail(model, child.getExternalKey(), cableBBService);
					} else if (child.getType().equals(SMPStaticIP.TYPE)) {
						new SMPStaticIP(model, child.getExternalKey(), cableBBService);
					} else if (child.getType().equals(SMPWiFi.TYPE)) {
						new SMPWiFi(model, child.getExternalKey(), cableBBService);
					} else if (child.getType().equals(BSA.TYPE)) {
						new BSA(model, child.getExternalKey(), cableBBService);
					} else {
						logger.warn("unknown CableBBService child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(CpeComposedService.TYPE)) {
				CpeComposedService cpeComposedService = new CpeComposedService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(HsdAccess.TYPE)) {
						new HsdAccess(model, child.getExternalKey(), cpeComposedService);
					} else if (child.getType().equals(VoipAccess.TYPE)) {
						new VoipAccess(model, child.getExternalKey(), cpeComposedService);
					} else {
						logger.warn("unknown CpeComposedService child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(CableVoiceService.TYPE)) {
				CableVoiceService cableVoiceService = new CableVoiceService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(DialToneAccess.TYPE)) {
						new DialToneAccess(model, child.getExternalKey(), cableVoiceService);
					} else if (child.getType().equals(SwitchFeature.TYPE)) {
						new SwitchFeature(model, child.getExternalKey(), cableVoiceService);
					} else if (child.getType().equals(VoiceMail.TYPE)) {
						new VoiceMail(model, child.getExternalKey(), cableVoiceService);
					} else {
						logger.warn("unknown CableVoiceService child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(MobileBBService.TYPE)) {
				MobileBBService mobileBBService = new MobileBBService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(SMPMobileBroadbandAttributes.TYPE)) {
						new SMPMobileBroadbandAttributes(model, child.getExternalKey(), mobileBBService);
					} else if (child.getType().equals(SMPMobileBroadbandDEF.TYPE)) {
						new SMPMobileBroadbandDEF(model, child.getExternalKey(), mobileBBService);
					} else {
						logger.warn("unknown MobileBBService child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(SMPSIMCard.TYPE)) {
				new SMPSIMCard(model, plan.getExternalKey());

			} else if (plan.getType().equals(ForeningsMailService.TYPE)) {
				ForeningsMailService service = new ForeningsMailService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(Mail.TYPE)) {
						new Mail(model, child.getExternalKey(), service);
					} else {
						logger.warn("unknown forenings mail child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(PlayService.TYPE)) {
				PlayService service = new PlayService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(Play.TYPE)) {
						new Play(model, child.getExternalKey(), service);
					}
				}
			} else if (plan.getType().equals(TdcMailService.TYPE)) {
				TdcMailService service = new TdcMailService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(TdcMail.TYPE)) {
						new TdcMail(model, child.getExternalKey(), service);
					} else if (child.getType().equals(TdcMailResource.TYPE)) {
						new TdcMailResource(model, child.getExternalKey(), service);
					}
				}
			} else if (plan.getType().equals(SikkerhedspakkeService.TYPE)) {
				SikkerhedspakkeService service = new SikkerhedspakkeService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(Sikkerhedspakke.TYPE)) {
						new Sikkerhedspakke(model, child.getExternalKey(), service);
					}
				}
			} else if (plan.getType().equals(MofiboService.TYPE)) {
				MofiboService service = new MofiboService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(Mofibo.TYPE)) {
						new Mofibo(model, child.getExternalKey(), service);
					}
				}
			} else {
				logger.warn("unknown service, type=" + plan.getType() + ", externalKey=" + plan.getExternalKey());
			}
		}
	}
}
