package dk.yousee.smp5.casemodel.vo.helpers;

import org.apache.log4j.Logger;

import dk.yousee.smp5.casemodel.SubscriberModel;
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
import dk.yousee.smp5.casemodel.vo.voiceline.MailBox;
import dk.yousee.smp5.casemodel.vo.voiceline.VoiceMail;
import dk.yousee.smp5.casemodel.vo.voiceline.VoiceService;
import dk.yousee.smp5.order.model.ResponseEntity;

/**
 * @author m64746
 *
 *         Date: 22/10/2015 Time: 13:57:12
 * 
 *         Helper to parse response from subscribers content
 */
public class Parse {

	private static final Logger logger = Logger.getLogger(Parse.class);
	private SubscriberModel model;

	public Parse(SubscriberModel model) {
		this.model = model;
	}

	/**
	 * Load the customer response into SubscriberModel.
	 *
	 * @param top
	 *            the node containing plans
	 */
	public void buildSubscriberModel(ResponseEntity top) {
		if (top == null)
			return;
		if (top.getLid() != null) {
			model.getSubscriber().setLid(top.getLid());
		}
		for (ResponseEntity plan : top.getEntities()) {
			logger.debug("Type" + plan.getType());
			logger.debug("Entity size: " + plan.getEntities().size());
			if (plan.getType().equals(SubContactSpec.TYPE)) {
				new SubContactSpec(model);
			} else if (plan.getType().equals(SubAddressSpec.TYPE)) {
				new SubAddressSpec(model);
			} else if (plan.getType().equals(SampSub.TYPE)) {
				new SampSub(model, plan.getExternalKey());
			} else if (plan.getType().equals(OTTService.TYPE)) {
				OTTService ottService = new OTTService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(OTTSubscription.TYPE)) {
						new OTTSubscription(model, child.getExternalKey(), ottService);
					} else {
						logger.warn("unknown OTTService child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(VideoComposedService.TYPE)) {
				VideoComposedService videoComposedService = new VideoComposedService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(VideoServicePlan.TYPE)) {
						VideoServicePlan videoServicePlan = new VideoServicePlan(model, child.getExternalKey(), videoComposedService);
						for (ResponseEntity subchild : child.getEntities()) {
							if (subchild.getType().equals(VideoServicePlanAttributes.TYPE)) {
								new VideoServicePlanAttributes(model, subchild.getExternalKey(), videoServicePlan);
							} else if (subchild.getType().equals(VideoSubscription.TYPE)) {
								new VideoSubscription(model, subchild.getExternalKey(), videoServicePlan);
							} else {
								logger.warn("unknown VideoServicePlan child_service " + child.getExternalKey());
							}
						}
					} else {
						logger.warn("unknown VideoComposedService child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(VideoCPEService.TYPE)) {
				VideoCPEService videoCPEService = new VideoCPEService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(VideoCPE.TYPE)) {
						VideoCPE videoCPE = new VideoCPE(model, child.getExternalKey(), videoCPEService);
						if (child.getEntities().size() > 1) {
							logger.warn("VideoCPE cannot have more than one child " + child.getExternalKey());
						}
						for (ResponseEntity subChild : child.getEntities()) {
							if (subChild.getType().equals(STBCas.TYPE)) {
								new STBCas(model, subChild.getExternalKey(), videoCPE);
							} else {
								logger.warn("unknown VideoCPE child_service " + child.getExternalKey());
							}
						}
					} else {
						logger.warn("unknown VideoCPEService child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(SmartCardService.TYPE)) {
				SmartCardService smartCardService = new SmartCardService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(SmartCard.TYPE)) {
						new SmartCard(model, child.getExternalKey(), smartCardService);
					} else {
						logger.warn("unknown SmartCardService child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(ForeningsMailService.TYPE)) {
				ForeningsMailService service = new ForeningsMailService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(Mail.TYPE)) {
						new Mail(model, child.getExternalKey(), service);
					} else {
						logger.warn("unknown forenings mail child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(SikkerhedspakkeService.TYPE)) {
				SikkerhedspakkeService service = new SikkerhedspakkeService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(Sikkerhedspakke.TYPE)) {
						new Sikkerhedspakke(model, child.getExternalKey(), service);
					}
				}
			} else if (plan.getType().equals(TdcMailService.TYPE)) {
				TdcMailService service = new TdcMailService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(TdcMail.TYPE)) {
						new TdcMail(model, child.getExternalKey(), service);
					}
				}
			} else if (plan.getType().equals(CableBBService.TYPE)) {
				CableBBService cableBBService = new CableBBService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(SMPStaticIP.TYPE)) {
						new SMPStaticIP(model, child.getExternalKey(), cableBBService);
					} else if (child.getType().equals(InetAccess.TYPE)) {
						new InetAccess(model, child.getExternalKey(), cableBBService);
					} else {
						logger.warn("unknown CableBBService child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(MTAService.TYPE)) {
				MTAService mtaService = new MTAService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(HsdAccess.TYPE)) {
						new HsdAccess(model, child.getExternalKey(), mtaService);
					} else if (child.getType().equals(DeviceControl.TYPE)) {
						new DeviceControl(model, child.getExternalKey(), mtaService);
					} else if (child.getType().equals(StdCpe.TYPE)) {
						new StdCpe(model, child.getExternalKey(), mtaService);
					} else if (child.getType().equals(VoipAccess.TYPE)) {
						new VoipAccess(model, child.getExternalKey(), mtaService);
					} else if (child.getType().equals(AddnCpe.TYPE)) {
						new AddnCpe(model, child.getExternalKey(), mtaService);
					} else {
						logger.warn("unknown MTAService child_service " + child.getExternalKey());
					}
				}
			} else if (plan.getType().equals(VoiceService.TYPE)) {
				VoiceService voiceService = new VoiceService(model, plan.getExternalKey());
				for (ResponseEntity child : plan.getEntities()) {
					if (child.getType().equals(SwitchFeature.TYPE)) {
						new SwitchFeature(model, child.getExternalKey(), voiceService);
					} else if (child.getType().equals(DialToneAccess.TYPE)) {
						new DialToneAccess(model, child.getExternalKey(), voiceService);
					} else if (child.getType().equals(VoiceMail.TYPE)) {
						VoiceMail voiceMail = new VoiceMail(model, child.getExternalKey(), voiceService);
						for (ResponseEntity secondchild : child.getEntities()) {
							if (secondchild.getType().equals(MailBox.TYPE)) {
								new MailBox(model, secondchild.getExternalKey(), voiceMail);
							} else {
								logger.warn("unknown VoiceMail child_service " + child.getExternalKey());
							}
						}
					} else {
						logger.warn("unknown VoiceService child_service " + child.getExternalKey());
					}
				}
			} else {
				logger.warn("unknown service, type=" + plan.getType() + ", externalKey=" + plan.getExternalKey());
			}
		}
	}
}
