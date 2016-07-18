package dk.yousee.smp.com;

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

import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.ExecuteOrderReply;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderData;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.SmpXml;
import dk.yousee.smp.order.model.Subscriber;
import dk.yousee.smp.order.util.OrderHelper;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: aka Date: 02/06/12 Time: 21.17 Add
 * subscriber to SMP
 */
public class AddSubscriberCom extends SmpCom<Order, ExecuteOrderReply> {

	private static final Logger logger = Logger.getLogger(AddSubscriberCom.class);

	@Override
	protected String convertRequest(Order order) {
		validate(order);
		return new Pack().generateRequest(order);
	}

	void validate(Order order) {
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
		public XmlObject createXml(Order order) {
			Integer orderId = -1;
			return createXmlOrder(order, orderId);
		}

		public XmlObject createXmlOrder(Order order, Integer orderId) {
			Subscriber subscriber = order.getSubscriber();
			SnapshotOrderValue ssOrderValue = createSnapshotOrderValue(order, orderId);
			SubType sSubType = addSubscriber(ssOrderValue, subscriber.getKundeId(), order.getExternalKey(), subscriber.getLid());
			// create address

			logger.debug("Searching for address in order");
			OrderData addressData = OrderHelper.findElementOfType(order, OrderDataLevel.ADDRESS);
			if (addressData != null) {
				logger.debug("Address object type: " + addressData.getType());
			} else {
				logger.debug("Address not found in order");
			}

			logger.debug("Searching for contact in order");
			OrderData contactData = OrderHelper.findElementOfType(order, OrderDataLevel.CONTACT);
			if (addressData != null) {
				logger.debug("Contact object type: " + contactData.getType());
			} else {
				logger.debug("Contact not found in order");
			}

			EntityListType entityList = sSubType.addNewEntityList();
			if (addressData != null) {
				logger.debug("Adding address");
				addAddressToEntityList(entityList, addressData);
			}
			// Create contact_on_address
			if (contactData != null) {
				logger.debug("Adding contact");
				addContactToEntityList(entityList, subscriber, contactData);
			}

			logger.debug("Adding samp sub");
			addSampSubToEntityList(entityList, subscriber, addressData);

			ExecuteOrderRequestDocument executeDoc = ExecuteOrderRequestDocument.Factory.newInstance();
			executeDoc.addNewExecuteOrderRequest().setOrderValue(ssOrderValue);
			return executeDoc;
		}

		public static SnapshotOrderValue createSnapshotOrderValue(Order order, Integer orderId) {
			SnapshotOrderValue ssOrderValue = SnapshotOrderValue.Factory.newInstance();
			ssOrderValue.setLastUpdateVersionNumber(-1);
			ssOrderValue.setApiClientId(order.getApiClientId());
			ssOrderValue.setOrderKey(headMaker.createOrderKey(orderId));
			ssOrderValue.setPriority(3);
			ssOrderValue.setDescription("TAYS DebugID - " + order.getDebugId());
			ssOrderValue.xsetOrderState(headMaker.createOrderStateType());
			headMaker.updateOrderParams(ssOrderValue.addNewOrderParamList(), order);
			return ssOrderValue;
		}

		static SubType addSubscriber(SnapshotOrderValue orderValue, Acct acct, String externalKey, String lid) {
			SubType sSubType = orderValue.addNewSubscriber();
			sSubType.setServiceProvider("YouSee");
			sSubType.setSubscriberType("residential");
			sSubType.setLocale("en_US");
			EntityKeyType entityKey = sSubType.addNewKey();
			SubKeyType entKey = SubKeyType.Factory.newInstance();
			entKey.setExternalKey(externalKey);
			entKey.setType("SubSpec:-");
			entityKey.set(entKey);
			sSubType.setState("active");
			EntityParamListType eParamList = sSubType.addNewParamList();
			ParamType parameter = eParamList.addNewParam();
			parameter.setName("acct");
			parameter.setStringValue(acct.toString());
			ParamType lidParm = eParamList.addNewParam();
			lidParm.setName("lid");
			lidParm.setStringValue(lid);
			return sSubType;
		}

		static void addSampSubToEntityList(EntityListType entityList, Subscriber subscriber, OrderData addressData) {
			EntityValue entityValue = entityList.addNewEntityValue();
			SubSvcType entity = addressMaker.createSampSubEntityValue(addressData, subscriber);
			entityValue.set(entity);
		}

		static void addAddressToEntityList(EntityListType entityList, OrderData addressData) {
			EntityValue entVal = entityList.addNewEntityValue();
			SubAddressType addressEntity = addressMaker.createAddressEntityValue(addressData);
			entVal.set(addressEntity);
		}

		static void addContactToEntityList(EntityListType entityList, Subscriber subscriber, OrderData contactData) {
			EntityValue entVal = entityList.addNewEntityValue();
			SubContactType contactType = contactMaker.createContactEntityValue(contactData, subscriber);
			entVal.set(contactType);
		}
	}

	// public OrderReply execute(Order order, Integer orderId) {
	// OrderData addressData = null;
	// OrderData contactData = null;
	// for (OrderData o : order.getOrderData()) {
	// if (o.getLevel() == OrderDataLevel.ADDRESS) {
	// addressData = o;
	// }
	// if (o.getLevel() == OrderDataLevel.CONTACT) {
	// contactData = o;
	// }
	// }
	//
	//
	// String xmlRequest = createXmlOrder(order, orderId);
	//
	//
	// OrderReply reply = new OrderReply();
	// reply.setXmlRequest(xmlRequest);
	//
	// OrderResult result = new OrderResult();
	// result.addOrderData(addressData);
	// result.addOrderData(contactData);
	// List<OrderResult> resList = new ArrayList<OrderResult>();
	// resList.add(result);
	// reply.setResult(resList);
	//
	// XmlObject xmlObject = executeAndParse(xmlRequest);
	// reply.setXmlResponse(xmlObject.toString());
	// OrderReply pasrsedReply = parseResult(xmlRequest, xmlObject.toString());
	// reply.setErrorMessage(pasrsedReply.getErrorMessage());
	// reply.setOrderId(pasrsedReply.getOrderId());
	// logger.info("Reply: " + reply);
	// return reply;
	// }

	@Override
	protected ExecuteOrderReply convertResponse(SmpXml xml, Order order) {

		// OrderData addressData = null;
		// OrderData contactData = null;
		// for (OrderData o : order.getOrderData()) {
		// if (o.getLevel() == OrderDataLevel.ADDRESS) {
		// addressData = o;
		// }
		// if (o.getLevel() == OrderDataLevel.CONTACT) {
		// contactData = o;
		// }
		// }
		ExecuteOrderReply reply;
		reply = new Parser().convertResponse(xml);
		return reply;
	}

	@Override
	public Integer getOperationTimeout() {
		return 50000;
	}

	private static class Parser extends OrderParser<ExecuteOrderReply> {

		@Override
		public ExecuteOrderReply convertResponse(SmpXml xml) {

			// OrderResult result = new OrderResult();
			// result.addOrderData(addressData);
			// result.addOrderData(contactData);
			// List<OrderResult> resList = new ArrayList<OrderResult>();
			// resList.add(result);
			// reply.setResult(resList);

			// XmlObject xmlObject = getCon().parseResponse(response);

			String response = xml.getResponse();
			XmlObject xmlObject = parseResponse(response);
			XmlObject[] res = xmlObject
					.selectPath("declare namespace smpsa='http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema'; $this//smpsa:executeOrderException");

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
				res = xmlObject
						.selectPath("declare namespace smpsa='http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema'; $this//smpsa:executeOrderResponse");
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
					// String name = node.getNodeName();
					String value = node.getFirstChild().getNodeValue();
					codelist.add(value);
				}

				NodeList nodeList2 = document.getElementsByTagName("smp:errorMessage");
				for (int s = 0; s < nodeList2.getLength(); s++) {
					Node node = nodeList2.item(s);
					// String name = node.getNodeName();
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

}
