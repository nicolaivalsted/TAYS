/**
 * 
 */
package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.OrderService;

/**
 * @author m64746
 *
 * Date: 20/10/2015
 * Time: 12:42:14
 */
public class HumaxSTBCase extends AbstractCase {
	
	public HumaxSTBCase(SubscriberModel model, OrderService service) {
		super(model, service);
		// TODO HumaxSTBCase
	}
	
	public HumaxSTBCase(Acct acct, OrderService service) {
        super(acct, service);
    }

}
