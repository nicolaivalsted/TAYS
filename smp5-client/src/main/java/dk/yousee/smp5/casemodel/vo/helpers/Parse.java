package dk.yousee.smp5.casemodel.vo.helpers;

import org.apache.log4j.Logger;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.base.SampSub;
import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp5.casemodel.vo.ott.OTTService;
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
		//TODO Add new services
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
				for(ResponseEntity child : plan.getEntities()){
					
				}
			} else {
				logger.warn("unknown service, type=" + plan.getType() + ", externalKey=" + plan.getExternalKey());
			}
		}
	}
}
