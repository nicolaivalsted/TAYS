package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.cpee.CpeComposedService;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.OrderService;

/**
 * Created by IntelliJ IDEA. User: aka Date: Oct 25, 2010 Time: 1:38:17 PM<br/>
 * Use case for global IP assignment
 */
public class GlobalIPCase extends AbstractCase {

	public GlobalIPCase(Acct acct, OrderService service) {
		super(acct, service);
	}

	/**
	 * Construct this case based on existing Subscriber Case<br/>
	 * This is a kind of chaining of use-cases. <br/>
	 * <p>
	 * First ask for the customer, eventually create him Then work with global
	 * IP.
	 * </p>
	 *
	 * @param customerCase
	 *            subscriber case's
	 */
	public GlobalIPCase(SubscriberCase customerCase) {
		super(customerCase.getModel(), customerCase.getService());
	}

	/**
	 * Update GI-Address
	 * 
	 * @param modemId
	 *            selected service plan instance (key is modemId)
	 * @param gi_address
	 *            the fixed IP address
	 * @return true if anything to do
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             on err
	 */
	public boolean updateGiAddress(ModemId modemId, String gi_address) throws BusinessException {
		boolean doAnything = false;

		CpeComposedService servicePlan = getModel().find().CpeComposedService(modemId);
		if (servicePlan != null) {
			HsdAccess hsdAccess = getModel().find().HsdAccess(modemId);
			if (hsdAccess != null) {
				doAnything = true;
				getModel().getOrder().getParams().put("clientType", "migrated_sub");
				hsdAccess.gi_address.setValue(gi_address);
				if (hsdAccess.service_on_address.get() == null) {
					hsdAccess.service_on_address.add(getModel().find().SubAddressSpec());
				}
			}
		}
		return doAnything;
	}
}
