package dk.yousee.smp5.com;

import dk.yousee.smp5.order.model.Order;

public class Smp5OrderConverter extends ProvisioningCom5 {

	public static String convert(Order order) {
		return new ProvisioningCom5().convertRequest(order);
	}

}