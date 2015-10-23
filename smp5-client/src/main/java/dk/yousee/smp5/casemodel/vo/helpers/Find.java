/**
 * 
 */
package dk.yousee.smp5.casemodel.vo.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.base.SampSub;
import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp5.casemodel.vo.ott.OTTService;
import dk.yousee.smp5.casemodel.vo.ott.OTTSubscription;
import dk.yousee.smp5.order.model.OrderDataType;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 13:37:25
 */
public class Find {
	private static final Logger logger = Logger.getLogger(Find.class);

	private List<BasicUnit> serviceLevelUnit;
	private Key key;

	public Find(SubscriberModel model, List<BasicUnit> serviceLevelUnit) {
		this.serviceLevelUnit = serviceLevelUnit;
		this.key = model.key();
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

	BasicUnit find(OrderDataType type) {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(type)) {
				return plan;
			}
		}
		return null;
	}

	// ======= Ott =======

	/**
	 * @return the OTTService the subscriber has
	 */
	public List<OTTService> OTTService() {
		List<OTTService> res = new ArrayList<OTTService>();
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(OTTService.TYPE)) {
				res.add((OTTService) plan);
			}
		}
		return res;
	}

	/**
	 * @param position
	 *            identifier for specific instance of the service plan
	 * @return instance if it exists
	 */
	public OTTService OTTService(BusinessPosition position) {
		List<OTTService> plans = OTTService();
		for (OTTService plan : plans) {
			if (position.equals(plan.getPosition())) {
				return plan;
			}
		}
		return null;
	}

	/**
	 * @param position
	 *            to service
	 * @return new instance
	 */
	public OTTSubscription Play(BusinessPosition position) {
		OTTService parent = OTTService(position);
		return parent == null ? null : parent.getOttSubscription();
	}

	BasicUnit find(OrderDataType type, String externalKey) {
		for (BasicUnit plan : serviceLevelUnit) {
			if (plan.getType().equals(type) && plan.getExternalKey().equals(externalKey)) {
				return plan;
			}
		}
		return null;
	}

}
