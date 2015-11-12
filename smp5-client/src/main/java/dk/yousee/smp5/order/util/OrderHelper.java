/**
 * 
 */
package dk.yousee.smp5.order.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderData;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;

/**
 * @author m64746
 *
 *         Date: 16/10/2015 Time: 13:06:53
 */
public class OrderHelper {

	public static OrderData createChildService(Action action, String externalKey, OrderDataType value) {
		return new OrderData(OrderDataLevel.CHILD_SERVICE, action, externalKey, value);
	}

	public static OrderData createContactData(Action action, String externalKey, OrderDataType type) {
		return new OrderData(OrderDataLevel.CONTACT, action, externalKey, type);
	}

	public static OrderData createService(Action action, String externalKey, OrderDataType value) {
		return new OrderData(OrderDataLevel.SERVICE, action, externalKey, value);
	}

	/**
	 * To create an address that is to be assigned to the subscriber an
	 * addressData element with children representing the elements of the
	 * address must be attached to the order
	 *
	 * @param action
	 *            activate, deactivate
	 * @param externalKey
	 *            the address Id, note the external key format in the external
	 *            key registry document.
	 * @param type
	 *            orderdata value
	 * @return OrderData element that needs the elements of the address as
	 *         children
	 */
	public static OrderData createAddressData(Action action, String externalKey, OrderDataType type) {
		return new OrderData(OrderDataLevel.ADDRESS, action, externalKey, type);
	}

	public static String generateOrderDateString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	public static String generateOrderDateStringFromString(String stringDate) {
		String dateFinal;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = formatter.parse(stringDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			dateFinal = sdf.format(date);
		} catch (ParseException e) {
			return null;
		}
		return dateFinal;
	}

	public static OrderData findElementOfType(Order order, OrderDataLevel levelToFind) {
		if (order != null) {
			for (OrderData o : order.getOrderData()) {
				if (o.getLevel() == levelToFind) {
					return o;
				}
			}
		}
		return null;
	}

}
