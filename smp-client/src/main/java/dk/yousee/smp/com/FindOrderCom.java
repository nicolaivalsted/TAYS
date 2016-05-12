package dk.yousee.smp.com;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ActionOrderValue;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyExceptionDocument;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyRequestDocument;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyResponseDocument;
import com.sun.java.products.oss.xml.serviceActivation.OrderKey;
import com.sun.java.products.oss.xml.serviceActivation.OrderStateType;

import dk.yousee.smp.order.model.OrderStateEnum;
import dk.yousee.smp.order.model.QueryOrderReply;
import dk.yousee.smp.order.model.SmpXml;

/**
 * Created with IntelliJ IDEA. User: aka Date: 01/06/12 Time: 21.30 Find order
 * in SMP
 */
public class FindOrderCom extends SmpCom<String, QueryOrderReply> {

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
	protected QueryOrderReply convertResponse(SmpXml xml, String orderID) {
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
		public QueryOrderReply convertResponse(SmpXml xml) {
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

	}

}
