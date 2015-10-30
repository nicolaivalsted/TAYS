/**
 * 
 */
package dk.yousee.smp5.casemodel.vo.helpers;

import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp5.casemodel.vo.base.SubSpec;
import dk.yousee.smp5.casemodel.vo.ott.OTTEntitlement;
import dk.yousee.smp5.casemodel.vo.ott.OTTService;
import dk.yousee.smp5.casemodel.vo.ott.OTTSubscription;
import dk.yousee.smp5.casemodel.vo.video.VideoComposedService;
import dk.yousee.smp5.casemodel.vo.video.VideoEvent;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlan;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.casemodel.vo.video.VideoSubscription;
import dk.yousee.smp5.casemodel.vo.stb.CpeConditionalAccess;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPE;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
import dk.yousee.smp5.casemodel.vo.stb.VideoCPEService;
import dk.yousee.smp5.casemodel.SubscriberModel;

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

	public OTTEntitlement OTTEntitlement(String rateCode) {
		OTTSubscription parent = model.alloc().OTTSubscription(rateCode);
		OTTEntitlement res = new OTTEntitlement(model, key.generateUUID(), parent);
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

	public VideoServicePlan VideoServicePlan(BusinessPosition position) {
		VideoComposedService parent = model.alloc().VideoComposedService(position);
		VideoServicePlan res = new VideoServicePlan(model, key.generateUUID(), position, parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public VideoComposedService VideoComposedService(BusinessPosition postion) {
		VideoComposedService res = new VideoComposedService(model, key.generateUUID());
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public VideoEvent VideoEvent(BusinessPosition position) {
		VideoServicePlan parent = model.alloc().VideoServicePlan(position);
		VideoEvent res = new VideoEvent(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public VideoServicePlanAttributes VideoServicePlanAttributes(BusinessPosition position) {
		VideoServicePlan parent = model.alloc().VideoServicePlan(position);
		VideoServicePlanAttributes res = new VideoServicePlanAttributes(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public VideoSubscription VideoSubscription(BusinessPosition position) {
		VideoServicePlan parent = model.alloc().VideoServicePlan(position);
		VideoSubscription res = new VideoSubscription(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public VideoCPE VideoCPE(String macAdress) {
		VideoCPEService parent = model.alloc().VideoCPEService();
		VideoCPE res = new VideoCPE(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public CpeConditionalAccess CpeConditionalAccess(String macAdress) {
		STBCas parent = model.alloc().STBCas(macAdress);
		CpeConditionalAccess res = new CpeConditionalAccess(model, key.generateUUID(), parent);
		if (res.getEntity() == null) {
			res.getDefaultOrderData();
		}
		return res;
	}

	public STBCas STBCas(String macAdress) {
		VideoCPE parent = model.alloc().VideoCPE(macAdress);
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

}
