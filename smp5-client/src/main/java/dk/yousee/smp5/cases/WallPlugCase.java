package dk.yousee.smp5.cases;

import org.apache.commons.lang3.StringUtils;

import dk.yousee.smp5.casemodel.vo.cablebb.CableBBService;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.OrderService;

/**
 *
 * @author m64746
 */
public class WallPlugCase extends AbstractCase {
	public WallPlugCase(OrderService orderService, Acct acct) {
		super(acct, orderService);
	}

	public WallPlugCase(SubscriberCase customerCase, boolean keepModel) {
		super(selectModel(customerCase.getModel(), keepModel), customerCase.getService());
	}

	/**
	 * @param sik
	 * @param signal
	 */
	public void updateWallplug(String sik, boolean signal) {
		CableBBService bbService = getModel().find().CableBBServiceSik(sik);
		String smpWallplug = bbService.getInetAccess().wallplug.getValue();
		boolean wallplug;
		if (StringUtils.isBlank(smpWallplug) || smpWallplug.equals("false")) {
			wallplug = false;
		} else {
			wallplug = true;
		}

		if (wallplug != signal) {
			bbService.getInetAccess().wallplug.setValue(String.valueOf(signal));
		}
	}
}
