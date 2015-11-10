package dk.yousee.smp5.casemodel.vo.helpers;

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
import dk.yousee.smp5.order.model.ResponseAssociation;
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
								VideoServicePlanAttributes vvv = new VideoServicePlanAttributes(model, subchild.getExternalKey(), videoServicePlan);
//								if(subchild.getAssociations().size() > 1){
//									logger.warn("VideoServicePlanAttributes cannot have more than one association " + subchild.getExternalKey());
//									ResponseAssociation association = subchild.getAssociations().get(0);
//									STBCas stbCas = new STBCas(model, externalKey, parent)
//									vvv.video_definition_has_cpe_conditional.add();
//								}
								ResponseAssociation association = subchild.getAssociations().get(0);
								//TODO ver como se trata das assoc aqui
								if(association != null){
									System.out.println("olha eu tenho uma assoc!!!!");
								}else{
									System.out.println("olha eu NAO tenho uma assoc!!!!");
								}
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
			} else {
				logger.warn("unknown service, type=" + plan.getType() + ", externalKey=" + plan.getExternalKey());
			}
		}
	}
}
