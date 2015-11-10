package dk.yousee.smp5.com;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ActionOrderValue;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyExceptionDocument;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyRequestDocument;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyResponseDocument;
import com.sun.java.products.oss.xml.serviceActivation.OrderKey;
import com.sun.java.products.oss.xml.serviceActivation.OrderStateType;

import dk.yousee.smp5.order.model.OrderStateEnum;
import dk.yousee.smp5.order.model.QueryOrderReply;
import dk.yousee.smp5.order.model.Smp5Xml;

/**
 * Created with IntelliJ IDEA. User: aka Date: 01/06/12 Time: 21.30 Find order
 * in SMP
 */
public class FindOrderCom extends Smp5Com<String, QueryOrderReply> {

	@Override
	protected String convertRequest(String orderID) {
		return new Pack().generateRequest(orderID);
	}

	private static class Pack extends RequestPack<String> {
		@Override
		public XmlObject createXml(String orderID) {
			return produceGetOrderByKeyRequest(orderID);
		}

		private GetOrderByKeyRequestDocument produceGetOrderByKeyRequest(String orderID) {
			GetOrderByKeyRequestDocument doc = GetOrderByKeyRequestDocument.Factory.newInstance();
			GetOrderByKeyRequestDocument.GetOrderByKeyRequest orderReq = doc.addNewGetOrderByKeyRequest();
			OrderKey ordKey = orderReq.addNewOrderKey();
			ordKey.setType("acct");
			ordKey.setPrimaryKey(orderID);
			return doc;
		}
	}

	@Override
	protected QueryOrderReply convertResponse(Smp5Xml xml, String orderID) {
		Parser parser = new Parser();
		return parser.convertResponse(xml);
	}

	@Override
	public Integer getOperationTimeout() {
		return 4000;
	}

	private static class Parser extends ResponseParser<QueryOrderReply> {

		private static final Logger logger = Logger.getLogger(Parser.class);

		@Override
		public QueryOrderReply convertResponse(Smp5Xml xml) {
			String response = xml.getResponse();
			XmlObject xmlObject = parseResponse(response);

			/* ERROR HANDLING */
			// for queryOrdersByOrderNumber errors
			XmlObject[] res = xmlObject
					.selectPath("declare namespace smpce='http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema'; $this//smpce:getOrderByKeyException");
			if (res.length > 0) {
				String errorMessage;
				// This is an error
				GetOrderByKeyExceptionDocument.GetOrderByKeyException ex = (GetOrderByKeyExceptionDocument.GetOrderByKeyException) res[0];
				if (ex.getRemoteException() != null) {
					errorMessage = ex.getRemoteException().getMessage();
				} else if (ex.getObjectNotFoundException() != null) {
					errorMessage = ex.getObjectNotFoundException().getMessage();
				} else if (ex.getIllegalArgumentException() != null) {
					errorMessage = ex.getIllegalArgumentException().getMessage();
				} else {
					errorMessage = null;
				}
				return new QueryOrderReply(errorMessage, xml);
			} else {
				/* PARSE RESULTS */
				// for queryOrdersByOrderNumber
				res = xmlObject
						.selectPath("declare namespace sa='http://java.sun.com/products/oss/xml/ServiceActivation'; $this//sa:getOrderByKeyResponse");
				logger.debug("Select getOrderByKeyResponse res length: " + res.length);
				if (res.length > 0) {
					GetOrderByKeyResponseDocument.GetOrderByKeyResponse entity = (GetOrderByKeyResponseDocument.GetOrderByKeyResponse) res[0];
					OrderStateEnum orderStateEnum = selectOrderState(entity);
					return new QueryOrderReply(orderStateEnum, xml);
				} else {
					return new QueryOrderReply("expected getOrderByKeyResponse", xml);
				}
			}
		}

		private OrderStateEnum selectOrderState(final GetOrderByKeyResponseDocument.GetOrderByKeyResponse entity) {
			if (entity.getOrderValue() instanceof ActionOrderValue) {
				ActionOrderValue actionOrder = (ActionOrderValue) entity.getOrderValue();
				OrderStateType.Enum anEnum = actionOrder.getOrderState();
				return OrderStateEnum.findOrderStateEnumByType(anEnum.intValue());
			} else {
				return null;
			}
		}

		private static String grepPrimaryKey(final String source) {
			final String key = "primaryKey>";
			int pos0 = source.indexOf(key);
			if (pos0 == -1)
				return null;
			pos0 = pos0 + key.length();
			int pos1 = source.indexOf("<", pos0);
			if (pos1 == -1)
				return null;
			return source.substring(pos0, pos1);
		}

	}

	/**
	 * What would be relevant to return back from this .... todo: Make this
	 * content..
	 */
	public static class OrderContent {
		private Integer orderId;
		private Date completion;
		private String state;
		private List<OrderParam> params;
		private List<OrderItem> items;
	}

	public static class OrderParam {
		private String name;
		private String value;
	}

	/**
	 * Each item should describe what the order item did.
	 */
	public static class OrderItem {
		private String type;
		private String action;
		private boolean hasImpact;
		private String itemState;
		private List<Param> params;
	}

	public static class Param {
		private String name;
		private String newValue;
		private String oldValue;
	}

}