/**
 * 
 */
package dk.yousee.smp5.casemodel.vo.helpers;

import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp5.casemodel.vo.base.SubSpec;
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

}
