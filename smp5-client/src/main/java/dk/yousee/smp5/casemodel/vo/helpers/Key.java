/**
 * 
 */
package dk.yousee.smp5.casemodel.vo.helpers;

import dk.yousee.smp5.casemodel.vo.helpers.UUIDGenerater;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.casemodel.SubscriberModel;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 13:27:28 Generate external keys to service
 *         plans TAYS External Keys name space registry.doc 4. Service
 *         definition
 */
public class Key {
	private SubscriberModel model;

	public Key(SubscriberModel model) {
		this.model = model;
	}

	public String getProvider() {
		if (model.getProvider().getProvider() != null) {
			return model.getProvider().getProvider();
		} else
			return "";
	}

	/**
	 * @param acct
	 *            - customer 9 digit number
	 * @return external Key
	 */
	public String SubscriberExternalKey(Acct acct) {
		return getProvider() + ":user_" + acct;
	}

	/**
	 * <p>
	 * Get a unique key to become key in sigma at various places
	 * </p>
	 * <p>
	 * When we introduced UUID it was decided not to set "YouSee:" as prefix.<br/>
	 * This was decided by Jacob 2010.10.25.
	 * </p>
	 * 
	 * @return generated Key
	 */
	public String generateUUID() {
		return UUIDGenerater.generateKey();
	}

	/**
	 * <p>
	 * Get a unique key to become key in sigma at various places
	 * </p>
	 * <p>
	 * This was created to inprove the massive update of subscriber video channels
	 * </p>
	 * 
	 * @return generated Key
	 */
	public String geenerateKeyVideo(String acct, String type) {
		StringBuilder str = new StringBuilder();
		if (type.equals("video_services_composed")) {
			str.append("VSC-");
		} else if (type.equals("video_service_plan")) {
			str.append("VSP-");
		} else if (type.equals("video_access")) {
			str.append("VA-");
		} else if (type.equals("video_subscription")) {
			str.append("VS-");
		}
		str.append(acct);
		return str.toString();
	}

}
