package dk.yousee.smp5.com;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

import com.sigmaSystems.schemas.x31.smpCommonValues.ParamListType;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamType;
import com.sigmaSystems.schemas.x31.smpCommonValues.SmpQueryValue;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ActionOrderValue;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.OrderItemType;
import com.sun.java.products.oss.xml.common.ArrayOfString;
import com.sun.java.products.oss.xml.serviceActivation.ArrayOfOrderValue;
import com.sun.java.products.oss.xml.serviceActivation.OrderValue;
import com.sun.java.products.oss.xml.serviceActivation.QueryOrdersExceptionDocument;
import com.sun.java.products.oss.xml.serviceActivation.QueryOrdersRequestDocument;
import com.sun.java.products.oss.xml.serviceActivation.QueryOrdersResponseDocument;

import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.OrderInfo;
import dk.yousee.smp5.order.model.OrderStateEnum;
import dk.yousee.smp5.order.model.QueryOrdersBySubscriberReply;
import dk.yousee.smp5.order.model.Smp5Xml;

/**
 * @author m64746
 *
 *         Date: 09/11/2015 Time: 17:58:48
 */
public class FindOrdersBySubscriberCom extends Smp5Com<Acct, QueryOrdersBySubscriberReply> {
	private static final Logger logger = Logger.getLogger(FindOrdersBySubscriberCom.class);
	private boolean open;

	public FindOrdersBySubscriberCom(boolean open) {
		this.open = open;
	}

	@Override
	protected String convertRequest(Acct acct) {
		return new Pack().generateRequest(acct);
	}

	@Override
	protected QueryOrdersBySubscriberReply convertResponse(Smp5Xml xml, Acct input) {
		return new Parser(open).convertResponse(xml);
	}

	@Override
	protected Integer getOperationTimeout() {
		return 4000;
	}

	private static final Set closedSet = new HashSet<OrderStateEnum>() {
		private static final long serialVersionUID = 2760836951133747710L;

		{
			add(OrderStateEnum.CLOSED);
			add(OrderStateEnum.CLOSED_ABORTED);
			add(OrderStateEnum.CLOSED_COMPLETED);
			add(OrderStateEnum.CLOSED_ABORTED_BYCLIENT);
			add(OrderStateEnum.CLOSED_ABORTED_BYSERVER);
		}
	};

	private class Pack extends RequestPack<Acct> {

		@Override
		public XmlObject createXml(Acct acct) {
			return queryOrdersBySubscriber(acct);
		}

		public QueryOrdersRequestDocument queryOrdersBySubscriber(Acct acct) {
			QueryOrdersRequestDocument doc = QueryOrdersRequestDocument.Factory.newInstance();
			QueryOrdersRequestDocument.QueryOrdersRequest orderReq = doc.addNewQueryOrdersRequest();
			SmpQueryValue smpQVal = SmpQueryValue.Factory.newInstance();
			smpQVal.setQueryName("OrderQueryByStatusAcct");
			ParamListType pList = smpQVal.addNewParamList();
			ParamType param = pList.addNewParam();
			param.setName("acct_no");
			param.setStringValue(acct.toString());
			param = pList.addNewParam();
			param.setName("state");
			param.setStringValue("*");

			orderReq.setQueryValue(smpQVal);
			ArrayOfString attribNames = orderReq.addNewAttributeNames();
			attribNames.addItem("managedEntityKey");
			attribNames.addItem("state");
			return doc;
		}

	}

	private static class Parser extends ResponseParser<QueryOrdersBySubscriberReply> {
		private boolean open;

		public Parser(boolean open) {
			this.open = open;
		}

		@Override
		public QueryOrdersBySubscriberReply convertResponse(Smp5Xml xml) {
			return parseResultForQueryOrdersBySubscriber(xml, open);
		}

		private QueryOrdersBySubscriberReply parseResultForQueryOrdersBySubscriber(Smp5Xml xml, boolean open2) {

			XmlObject xmlObject = parseResponse(xml.getResponse());

			/* ERROR HANDLING */
			XmlObject[] res;
			// for queryOrdersBySubscriber errors
			res = xmlObject.selectPath("declare namespace smpce='http://java.sun.com/products/oss/xml/ServiceActivation'; $this//smpce:queryOrdersException");

			if (res.length > 0) {
				String errorMessage;
				// This is an error
				QueryOrdersExceptionDocument.QueryOrdersException ex = (QueryOrdersExceptionDocument.QueryOrdersException) res[0];
				if (ex.getRemoteException() != null) {
					errorMessage = ex.getRemoteException().getMessage();
				} else if (ex.getIllegalArgumentException() != null) {
					errorMessage = ex.getIllegalArgumentException().getMessage();
				} else {
					errorMessage = null;
				}
				return new QueryOrdersBySubscriberReply(errorMessage, xml);
			} else {
				/* PARSE RESULTS */
				// for queryOrdersBySubscriber
				res = xmlObject.selectPath("declare namespace sa='http://java.sun.com/products/oss/xml/ServiceActivation'; $this//sa:queryOrdersResponse");
				logger.debug("Select queryOrdersResponse res length: " + res.length);
				if (res.length > 0) {
					QueryOrdersResponseDocument.QueryOrdersResponse entity = (QueryOrdersResponseDocument.QueryOrdersResponse) res[0];
					List<OrderInfo> list = parseQueryOrdersBySubscriber(entity, open);
					return new QueryOrdersBySubscriberReply(list, xml);
				} else {
					return new QueryOrdersBySubscriberReply("expected queryOrdersResponse in response", xml);
				}
			}
		}

		public List<OrderInfo> parseQueryOrdersBySubscriber(QueryOrdersResponseDocument.QueryOrdersResponse queryOrdersResponse, boolean open) {

			List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
			ArrayOfOrderValue arrayOfOrderValue = queryOrdersResponse.getOrderValue();

			for (OrderValue orderVal : arrayOfOrderValue.getItemArray()) {
				int orderState = orderVal.getOrderState().intValue();
				OrderStateEnum stateEnum = OrderStateEnum.findOrderStateEnumByType(orderState);
				String orderId = orderVal.getOrderKey().getPrimaryKey();

				if (!closedSet.contains(stateEnum) || !open) {
					OrderInfo orderInfo = new OrderInfo();
					ActionOrderValue actionOrderValue = (ActionOrderValue) orderVal;

					ActionOrderValue.OrderItemList orderItemList = actionOrderValue.getOrderItemList();
					if (orderItemList != null && orderItemList.getOrderItemArray() != null)
						for (OrderItemType orderItemType : orderItemList.getOrderItemArray()) {
							String state = orderItemType.getItemState();
							if (state != null && state.contains("manualtask")) {
								orderInfo.setManualtask(true);
							}
						}
					Date orderDate = null;
					if (actionOrderValue.getAuditInfo() != null && actionOrderValue.getAuditInfo().getCreatedDateTime() != null)
						orderDate = actionOrderValue.getAuditInfo().getCreatedDateTime().getTime();
					Date completionDate = null;
					if (orderVal.getActualCompletionDate() != null)
						completionDate = orderVal.getActualCompletionDate().getTime();

					orderInfo.setCompletionDate(completionDate);
					orderInfo.setOrderDate(orderDate);
					orderInfo.setOrderId(orderId);
					orderInfo.setStateEnum(stateEnum);
					orderInfos.add(orderInfo);
				}
			}
			return orderInfos;
		}

	}

}
