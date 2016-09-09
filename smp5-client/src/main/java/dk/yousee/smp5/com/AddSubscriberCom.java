/**
 * 
 */
package dk.yousee.smp5.com;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityKeyType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityParamListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubAddressType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubContactType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubKeyType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcType;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamType;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ExecuteOrderExceptionDocument;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ExecuteOrderRequestDocument;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ExecuteOrderResponseDocument;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.SnapshotOrderValue;
import com.sun.java.products.oss.xml.cbe.core.EntityValue;

import dk.yousee.smp5.order.model.Constants;
import dk.yousee.smp5.order.model.ExecuteOrderReply;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderData;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.Smp5Xml;
import dk.yousee.smp5.order.model.Subscriber;
import dk.yousee.smp5.order.util.OrderHelper;

/**
 * @author m64746
 *
 *         Date: 16/10/2015 Time: 12:35:23
 */
public class AddSubscriberCom extends Smp5Com<Order, ExecuteOrderReply> {
	private static final Logger logger = Logger.getLogger(AddSubscriberCom.class);

	@Override
	protected String convertRequest(Order input) {
		validate(input);
		return new Pack().generateRequest(input);
	}

	@Override
	protected ExecuteOrderReply convertResponse(Smp5Xml xml, Order input) {
		ExecuteOrderReply reply;
		reply = new Parser().convertResponse(xml);
		return reply;
	}

	@Override
	protected Integer getOperationTimeout() {
		return 50000;
	}

	public static String makePrettyErrorMessage(String message, String response) {
		if (response != null && response.trim().length() != 0) {
			try {
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder;
				docBuilder = docBuilderFactory.newDocumentBuilder();
				Document document = docBuilder.parse(new InputSource(new StringReader(response)));
				NodeList nodeList = document.getElementsByTagName("smp:errorCode");
				List<String> codelist = new ArrayList<String>();
				List<String> messagelist = new ArrayList<String>();

				for (int s = 0; s < nodeList.getLength(); s++) {
					Node node = nodeList.item(s);
					String value = node.getFirstChild().getNodeValue();
					codelist.add(value);
				}

				NodeList nodeList2 = document.getElementsByTagName("smp:errorMessage");
				for (int s = 0; s < nodeList2.getLength(); s++) {
					Node node = nodeList2.item(s);
					String value = node.getFirstChild().getNodeValue();
					messagelist.add(value);
				}
				for (int i = 0; i < codelist.size(); i++) {
					message = message + "\n" + "errorcode = " + codelist.get(i) + "  errorMessage = " + messagelist.get(i);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return message;
	}

	private void validate(Order order) {
		if (order == null || order.getOrderData() == null || order.getOrderData().size() == 0) {
			throw new IllegalArgumentException("Trying to create subcriber with no order data!");
		}
		if (OrderHelper.findElementOfType(order, OrderDataLevel.ADDRESS) == null) {
			throw new IllegalArgumentException("An element of orderData must be [address] with list of address parameter");
		}
		if (OrderHelper.findElementOfType(order, OrderDataLevel.CONTACT) == null) {
			throw new IllegalArgumentException("An element of orderData must be [contact] with list of contact parameter");
		}
		if (order.getSubscriber() == null) {
			throw new IllegalArgumentException("Subscriber must be included!");
		}
	}

	private static class Pack extends RequestPack<Order> {
		private static final HeadMaker headMaker = new HeadMaker();
		private static final ContactMaker contactMaker = new ContactMaker();
		private static final AddressMaker addressMaker = new AddressMaker();

		@Override
		public XmlObject createXml(Order input) {
			Integer orderId = -1;
			return createXmlOrder(input, orderId);
		}

		public XmlObject createXmlOrder(Order order, Integer orderId) {
			Subscriber subscriber = order.getSubscriber();
			SnapshotOrderValue ssOrderValue = createSnapshotOrderValue(order, orderId);
			SubType sSubType = addSubscriber(ssOrderValue, order.getExternalKey(), subscriber);

			OrderData addressData = OrderHelper.findElementOfType(order, OrderDataLevel.ADDRESS);
			OrderData contactData = OrderHelper.findElementOfType(order, OrderDataLevel.CONTACT);
			EntityListType entityList = sSubType.addNewEntityList();

			if (addressData != null) {
				addAddressToEntityList(entityList, addressData);
			}
			if (contactData != null) {
				addContactToEntityList(entityList, subscriber, contactData);
			}
			addSampSubToEntityList(entityList, subscriber, addressData);
			ExecuteOrderRequestDocument executeDoc = ExecuteOrderRequestDocument.Factory.newInstance();
			executeDoc.addNewExecuteOrderRequest().setOrderValue(ssOrderValue);
			return executeDoc;
		}

		/**
		 * @param entityList
		 * @param subscriber
		 * @param addressData
		 */
		private void addSampSubToEntityList(EntityListType entityList, Subscriber subscriber, OrderData addressData) {
			EntityValue entityValue = entityList.addNewEntityValue();
			SubSvcType entity = addressMaker.createSampSubEntityValue(addressData, subscriber);
			entityValue.set(entity);
		}

		/**
		 * @param entityList
		 * @param subscriber
		 * @param contactData
		 */
		private void addContactToEntityList(EntityListType entityList, Subscriber subscriber, OrderData contactData) {
			EntityValue entVal = entityList.addNewEntityValue();
			SubContactType contactType = contactMaker.createContactEntityValue(contactData, subscriber);
			entVal.set(contactType);
		}

		/**
		 * @param entityList
		 * @param addressData
		 */
		private void addAddressToEntityList(EntityListType entityList, OrderData addressData) {
			EntityValue entVal = entityList.addNewEntityValue();
			SubAddressType addressEntity = addressMaker.createAddressEntityValue(addressData);
			entVal.set(addressEntity);
		}

		/**
		 * @param ssOrderValue
		 * @param kundeId
		 * @param externalKey
		 * @return
		 */
		private SubType addSubscriber(SnapshotOrderValue ssOrderValue, String externalKey, Subscriber subscriber) {
			SubType sSubType = ssOrderValue.addNewSubscriber();
			sSubType.setSubscriberType(Constants.SUBSCRIBER_TYPE);
			sSubType.setLocale("en_US");
			EntityKeyType entityKey = sSubType.addNewKey();
			SubKeyType entKey = SubKeyType.Factory.newInstance();
			entKey.setExternalKey(externalKey);
			entKey.setType(Constants.SUBKEY_TYPE);
			entityKey.set(entKey);
			sSubType.setState("active");
			EntityParamListType eParamList = sSubType.addNewParamList();
			ParamType parameter = eParamList.addNewParam();
			parameter.setName("acct");
			parameter.setStringValue(subscriber.getKundeId().toString());
			ParamType lidParm = eParamList.addNewParam();
			lidParm.setName("lid");
			lidParm.setStringValue(subscriber.getLid());
			return sSubType;
		}

		/**
		 * @param order
		 * @param orderId
		 * @return SnapshotOrderValue
		 */
		private SnapshotOrderValue createSnapshotOrderValue(Order order, Integer orderId) {
			SnapshotOrderValue ssOrderValue = SnapshotOrderValue.Factory.newInstance();
			ssOrderValue.setLastUpdateVersionNumber(-1);
			ssOrderValue.setApiClientId(order.getApiClientId());
			ssOrderValue.setOrderKey(headMaker.createOrderKey(orderId));
			ssOrderValue.setPriority(3);
			ssOrderValue.setDescription("TAYS DebugId - " + order.getDebugId());
			ssOrderValue.xsetOrderState(headMaker.createOrderStateType());
			headMaker.updateOrderParams(ssOrderValue.addNewOrderParamList(), order);
			return ssOrderValue;
		}

	}

	private static class Parser extends OrderParser<ExecuteOrderReply> {

		@Override
		public ExecuteOrderReply convertResponse(Smp5Xml xml) {
			String response = xml.getResponse();
			XmlObject xmlObject = parseResponse(response);
			XmlObject[] res = xmlObject.selectPath(
					"declare namespace smpsa='http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema'; $this//smpsa:executeOrderException");

			if (res.length > 0) {
				// This is an error
				String errorMessage;
				ExecuteOrderExceptionDocument.ExecuteOrderException ex = (ExecuteOrderExceptionDocument.ExecuteOrderException) res[0];
				if (ex.getRemoteException() != null) {
					errorMessage = ex.getRemoteException().getMessage();
				} else if (ex.getObjectNotFoundException() != null) {
					errorMessage = ex.getObjectNotFoundException().getMessage();

				} else if (ex.getIllegalArgumentException() != null) {
					errorMessage = ex.getIllegalArgumentException().getMessage();
				} else {
					errorMessage = null;
				}
				ExecuteOrderReply reply;
				reply = new ExecuteOrderReply(errorMessage, xml);
				return reply;

			} else {
				res = xmlObject.selectPath(
						"declare namespace smpsa='http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema'; $this//smpsa:executeOrderResponse");
				if (res.length > 0) {
					// Everything aparantly went fine
					logger.debug("Got ExecuteOrderResponse back!");
					ExecuteOrderResponseDocument.ExecuteOrderResponse resp = (ExecuteOrderResponseDocument.ExecuteOrderResponse) res[0];
					ExecuteOrderReply.MadeOrder eor = parseMadeOrder(resp);
					return new ExecuteOrderReply(eor, xml);
				} else {
					return new ExecuteOrderReply("Error in parsing result, no order", xml);
				}
			}
		}

	}

}
