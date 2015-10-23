package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.OrderService;

public class VideoCase extends AbstractCase {
	
	public VideoCase(SubscriberModel model, OrderService service) {
		super(model, service);
		// TODO VideoCase
	}
	
	public VideoCase(Acct acct, OrderService service) {
        super(acct, service);
    }

}
