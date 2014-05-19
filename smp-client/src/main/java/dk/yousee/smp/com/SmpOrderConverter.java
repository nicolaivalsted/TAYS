package dk.yousee.smp.com;

import dk.yousee.smp.order.model.Order;

public class SmpOrderConverter extends ProvisioningCom {
	
	public static String convert(Order order) {
		return new ProvisioningCom().convertRequest(order);
	}

}
