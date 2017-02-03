package dk.yousee.smp5.cases;

import org.apache.commons.lang3.StringUtils;

import dk.yousee.smp5.casemodel.vo.cablebb.CableBBService;
import dk.yousee.smp5.casemodel.vo.cablebb.InetAccess;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.OrderService;

/**
 *
 * @author m64746
 */
public class InternetSubServicesCase extends AbstractCase {
	public InternetSubServicesCase(OrderService orderService, Acct acct) {
		super(acct, orderService);
	}

	public InternetSubServicesCase(SubscriberCase customerCase, boolean keepModel) {
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

	public void updateEmailUnblock(String sik, boolean signal) {
		CableBBService bbService = getModel().find().CableBBServiceSik(sik);
		String smpWallplug = bbService.getInetAccess().email_server_enable.getValue();
		boolean wallplug;
		if (StringUtils.isBlank(smpWallplug) || smpWallplug.equals("false")) {
			wallplug = false;
		} else {
			wallplug = true;
		}

		if (wallplug != signal) {
			bbService.getInetAccess().email_server_enable.setValue(String.valueOf(signal));
		}
	}

	public void updateWifiOff(String sik, boolean signal) {
		InetAccess inetAccess = getModel().find().CableBBServiceSik(sik).getInetAccess();
		String smpWallplug = inetAccess.wifi_security_disabled.getValue();
		boolean wallplug;
		if (StringUtils.isBlank(smpWallplug) || smpWallplug.equals("false")) {
			wallplug = false;
		} else {
			wallplug = true;
		}

		if (wallplug != signal) {
			inetAccess.wifi_security_disabled.setValue(String.valueOf(signal));

			if (!signal && StringUtils.isBlank(inetAccess.ss_id.getValue())) {
				inetAccess.ss_id.setValue(InetAccess.generateSsid());
				inetAccess.psk.setValue(InetAccess.generatePsk());
				inetAccess.gw_channel_id.setValue("0");
			}

		}
	}

}