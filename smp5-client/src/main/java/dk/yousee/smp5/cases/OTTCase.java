package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.OrderService;

/**
 * @author m64746
 *
 * Date: 20/10/2015
 * Time: 12:40:58
 */
public class OTTCase extends AbstractCase {
	
	public OTTCase(SubscriberModel model, OrderService service) {
		super(model, service);
		// TODO OTTCase
	}
	
	public OTTCase(Acct acct, OrderService service) {
        super(acct, service);
    }

}
