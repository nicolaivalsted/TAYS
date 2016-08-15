package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.ModemId;
import dk.yousee.smp5.casemodel.vo.cablebb.CableBBService;
import dk.yousee.smp5.casemodel.vo.cablebb.InetAccess;
import dk.yousee.smp5.casemodel.vo.emta.HsdAccess;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.OrderService;


/**
 *
 * @author m27236
 */
public class CableBBActivationCase extends MacAddressCase {

	public CableBBActivationCase(Acct acct, OrderService orderService) {
		super(acct, orderService);
	}

	public CableBBActivationCase(SubscriberModel model, OrderService orderService) {
		super(model, orderService);
	}

	public Boolean activateM5cmModem(MacAddressCase.HsdAccessData hsdAccessData, CableBBService cableBBService) throws BusinessException {
		Boolean hasImpact = null;

		HsdAccess hsdAccess = null;
		if (getModel().find().HsdAccess(cableBBService.getModemId()) != null) {
			// cahnge modem
			if (getModel().find().HsdAccess(cableBBService.getModemId()).data_port_id.getValue() != null) {
				String oldmac = getModel().find().HsdAccess(cableBBService.getModemId()).data_port_id.getValue();

				if (!oldmac.equals(hsdAccessData.getCm_mac())) {
					hsdAccess = assignCMMacAddressForHsdAccess(hsdAccessData.getCm_mac(), hsdAccessData, cableBBService.getModemId(),"");
					hasImpact = true;
				}
			}
		} else {
			// new customer no modem
			hsdAccess = addHsdAccess(cableBBService.getModemId(), hsdAccessData);
			hasImpact = true;
		}

		if (hsdAccess != null) {
			addAssocInternet_access_has_emta_cmForInetAccess(hsdAccess, cableBBService.getModemId());
		}

		return hasImpact;
	}

	@Override
	public InetAccess addAssocInternet_access_has_emta_cmForInetAccess(HsdAccess hsdAccess, ModemId modemId) throws BusinessException {
		InetAccess inetAccess = getModel().find().InetAccess(modemId);
		if (inetAccess.internet_access_has_emta_cm.get() == null) {
			inetAccess.internet_access_has_emta_cm.add(hsdAccess);
		}
		return inetAccess;
	}

}
