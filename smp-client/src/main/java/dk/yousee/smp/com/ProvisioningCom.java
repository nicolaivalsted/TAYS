package dk.yousee.smp.com;

import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityKeyType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityParamListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubAddressKeyType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubAddressType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubContactKeyType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubContactType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubKeyType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcKeyType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcType;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamType;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ActionOrderValue;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ExecuteOrderExceptionDocument;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ExecuteOrderRequestDocument;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ExecuteOrderResponseDocument;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.OrderItemType;
import com.sun.java.products.oss.xml.cbe.core.EntityValue;
import com.sun.java.products.oss.xml.serviceActivation.OrderValue;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.ExecuteOrderReply;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderData;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.Priority;
import dk.yousee.smp.order.model.ServicePrefix;
import dk.yousee.smp.order.model.SmpXml;
import dk.yousee.smp.order.model.Subscriber;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 02/06/12
 * Time: 20.07
 * Communication to provisioning
 */
public class ProvisioningCom extends SmpCom<Order, ExecuteOrderReply> {

    private static final Logger logger = Logger.getLogger(ProvisioningCom.class);

    @Override
    protected String convertRequest(Order order) {
        if (order.isAsynchronous()) throw new IllegalArgumentException("Asynchronious is not implemented");
        if (order.getType() == null) {
            throw new IllegalArgumentException("Order has no type");
        }
        return new Pack().generateRequest(order);
    }


    private static class Pack extends RequestPack<Order> {

        private static final ServiceStateTypeConverter sstc = new ServiceStateTypeConverter();
        private static final SmpManipulationActionFactory samf = new SmpManipulationActionFactory();
        private static final HeadMaker headMaker = new HeadMaker();
        private static final ContactMaker contactMaker = new ContactMaker();
        private static final AddressMaker addressMaker = new AddressMaker();
        private static final AssociationMaker associationMaker = new AssociationMaker();
        private static final XmlMaker myMaker = new XmlMaker(){};

        @Override
        public XmlObject createXml(Order order) {
            Integer orderId = -1;
            ExecuteOrderRequestDocument execDoc;
            execDoc = createXmlOrderDoc(order, orderId);
            return execDoc;
        }

        /**
         * Will create the XML document to be transferred to Sigma in order to provisioning this order
         *
         * @param order   the order document from client
         * @param orderId ordernumber (always -1)
         * @return xml document to be processed at Sigma
         */
        private ExecuteOrderRequestDocument createXmlOrderDoc(Order order, Integer orderId) {
            OrderValue orderValue;
            orderValue = createForExistingCustomer(order, orderId);
            ExecuteOrderRequestDocument execDoc = ExecuteOrderRequestDocument.Factory.newInstance();
            ExecuteOrderRequestDocument.ExecuteOrderRequest execRequest = execDoc.addNewExecuteOrderRequest();
            execRequest.setOrderValue(orderValue);
            return execDoc;
        }

        private OrderValue createForExistingCustomer(Order order, Integer orderId) {
            Subscriber subscriber = order.getSubscriber();
            ActionOrderValue actionOrder = createActionOrderValue(order, orderId);
            ActionOrderValue.OrderItemList orderItemList = actionOrder.addNewOrderItemList();
            logger.debug("Looping through orderData");
            for (OrderData plan : order.getOrderData()) {
                logger.debug("Service type and externalKey: " + plan.getType() + plan.getExternalKey());
                if (plan.getLevel() == OrderDataLevel.CONTACT) {
                    addContactToOrderItem(orderItemList, plan, order);
                } else if (plan.getLevel() == OrderDataLevel.ADDRESS) {
                    addAddressToOrderItem(orderItemList, plan, order);
                } else if (plan.getLevel() == OrderDataLevel.SUBSPEC) {
                    // todo: THIS MUST BE A BUG !!!!!!!
                    deleteSubscription(orderItemList, plan, subscriber.getKundeId().toString());
                } else if (plan.getLevel() == OrderDataLevel.SERVICE) {
                    addServiceRequest(orderItemList, plan);
                } else if (plan.getLevel() == OrderDataLevel.SERVICE_HIDDEN) {
                    for (OrderData o : plan.getChildren()) {
                        addServiceRequest(orderItemList, o);
                    }
                }
            }
            return actionOrder;
        }
        /**
         * Create the orders outer most xml element
         * @param order order data from client
         * @param orderId order number
         * @return XML element
         */
        private static ActionOrderValue createActionOrderValue(Order order, Integer orderId) {
            ActionOrderValue actionOrder = ActionOrderValue.Factory.newInstance();
            actionOrder.setLastUpdateVersionNumber(-1);
            actionOrder.setApiClientId(order.getApiClientId());
            actionOrder.setOrderKey(headMaker.createOrderKey(orderId));
            actionOrder.setPriority(order.getPriority().asInt());
            actionOrder.xsetOrderState(headMaker.createOrderStateType());
            SubKeyType subKey = actionOrder.addNewSubKey();
            subKey.setType("SubSpec:-");
            subKey.setExternalKey(order.getExternalKey());
            headMaker.updateOrderParams(actionOrder.addNewOrderParamList(), order);
            return actionOrder;
        }

        static void addContactToOrderItem(ActionOrderValue.OrderItemList orderItemList, OrderData plan, Order order) {
            OrderItemType itemType = orderItemList.addNewOrderItem();
            itemType.setAction(samf.toAction(plan.getAction()).getValue());
            addContact(itemType, order.getSubscriber(), plan);
        }

        static void addContact(OrderItemType orderItem, Subscriber subscriber, OrderData contactData) {
            SubContactType contactType = contactMaker.createContactEntityValue(contactData, subscriber);
            SubContactKeyType subContactKey = contactMaker.createContactKey();
            orderItem.setEntityValue(contactType);
            orderItem.setEntityKey(subContactKey);
            orderItem.setItemState("open.not_running.not_started");
        }

        static void addAddressToOrderItem(ActionOrderValue.OrderItemList orderItemList, OrderData plan, Order order) {
            OrderItemType itemType = orderItemList.addNewOrderItem();
            itemType.setAction(samf.toAction(plan.getAction()).getValue());
            addAddress(itemType, plan);
            if (plan.getAction().equals(Action.ACTIVATE)) {
                itemType = orderItemList.addNewOrderItem();
                addressMaker.addSampSub(itemType, order.getSubscriber(), plan);
            }
        }

        static void addAddress(OrderItemType orderItem, OrderData addressData) {
            SubAddressType addressEntity = addressMaker.createAddressEntityValue(addressData);
            SubAddressKeyType subAddressKeyType = addressMaker.createAddressKey();
            orderItem.setEntityKey(subAddressKeyType);
            orderItem.setEntityValue(addressEntity);
            orderItem.setItemState("open.not_running.not_started");
        }

        /**
         * Produces xml for a service plan
         *
         * @param orderItemList list to add plan to. Precondition: N elements, Postcondition: N+1 elements
         * @param servicePlan   client input to be converted to XML
         */
        static void addServiceRequest(ActionOrderValue.OrderItemList orderItemList
            , OrderData servicePlan) {
            if (servicePlan.getType().getType().equalsIgnoreCase(new OrderDataType(ServicePrefix.SubSvcSpec, "samp_sub").getType())) {
                addServiceRequestForSampSub(orderItemList, servicePlan);
                return;
            }

            SubSvcKeyType subSvcKeyType = myMaker.initiateSubSvcKeyType(
                servicePlan.getType(), servicePlan.getExternalKey());
            OrderItemType servicePart = orderItemList.addNewOrderItem();
            servicePart.setAction(samf.toAction(servicePlan.getAction()).getValue());
            servicePart.setEntityKey(subSvcKeyType);
            logger.debug("ServiceData constains: "
                + "Level: " + servicePlan.getLevel() + ",Action: " + servicePlan.getAction() + ",State: " + servicePlan.getState()
                + ",Type: " + servicePlan.getType() + ",ExternalKey: " + servicePlan.getExternalKey());
            //Add service to request
            SubSvcType serviceEntity = SubSvcType.Factory.newInstance();
            serviceEntity.xsetServiceState(sstc.toState(servicePlan.getAction()));
            serviceEntity.setServiceKey(subSvcKeyType);

            OrderData parentServiceKey = servicePlan.getParent();
            if (parentServiceKey != null) {
                serviceEntity.setParentServiceKey(myMaker.initiateSubSvcKeyType(OrderDataType.SERVICE_TYPE_PARENT_SERVICE_KEY, parentServiceKey.getExternalKey()));
            }
            addServices(serviceEntity, servicePlan);

            EntityParamListType paramList = EntityParamListType.Factory.newInstance();
            myMaker.addParameters(paramList, servicePlan.getParams());
            if (paramList.getParamArray().length > 0) {
                serviceEntity.setParamList(paramList);
            }
            AssocListType assocList = associationMaker.createAssociationsFromOrderDataAssociations(servicePlan.getAssociations());
            if (assocList.getAssociationArray().length > 0) {
                AssocListType newAssocList = serviceEntity.addNewAssociationList();
                newAssocList.setAssociationArray(assocList.getAssociationArray());
            }
            servicePart.setEntityValue(serviceEntity);
        }

        private static void addServiceRequestForSampSub(ActionOrderValue.OrderItemList orderItemList
            , OrderData servicePlan) {

            SubSvcKeyType subSvcKeyType = myMaker.initiateSubSvcKeyType(
                servicePlan.getType(), servicePlan.getExternalKey());
            OrderItemType servicePart = orderItemList.addNewOrderItem();
            servicePart.setAction(samf.toAction(servicePlan.getAction()).getValue());
            servicePart.setEntityKey(subSvcKeyType);

            SubSvcType serviceEntity = SubSvcType.Factory.newInstance();
            serviceEntity.xsetServiceState(sstc.toState(servicePlan.getAction()));
            serviceEntity.setServiceKey(subSvcKeyType);

            AssocListType associations = serviceEntity.addNewAssociationList();
            AssocType addressAddressAssoc = associations.addNewAssociation();
            addressAddressAssoc.setAssociationType("service_on_address");
            SubAddressKeyType zEndKey = SubAddressKeyType.Factory.newInstance();
            zEndKey.setType("SubAddressSpec:-");
            zEndKey.setExternalKey("primary");
            addressAddressAssoc.setZEndKey(zEndKey);
            servicePart.setEntityValue(serviceEntity);
        }

        private static void addServices(SubSvcType serviceEntity, OrderData servicePlan) {
            logger.debug("Adding child services, serviceData has " + servicePlan.getChildren().size() + " children");

            SubSvcType.ChildServiceList serviceList = serviceEntity.addNewChildServiceList();
            for (OrderData child : servicePlan.getChildren()) {
                if (OrderDataLevel.CHILD_SERVICE == child.getLevel()) {
                    SubSvcType childService;
                    logger.debug("Adding child service of type " + child.getType());
                    childService = serviceList.addNewServiceValue();
                    childService.xsetServiceState(sstc.toState(child.getAction()));
                    SubSvcKeyType serviceKey = childService.addNewServiceKey();
                    String serviceType = child.getType().toString();
                    if (child.getExternalKey() != null) {
                        serviceKey.setExternalKey(child.getExternalKey());
                    } else {
                        throw new IllegalArgumentException("AddServices. ExternalKey can't be null");
                    }
                    serviceKey.setType(serviceType);

                    SubSvcKeyType parentServiceKey = childService.addNewParentServiceKey();
                    parentServiceKey.setType(servicePlan.getType().toString());
                    parentServiceKey.setExternalKey(servicePlan.getExternalKey());

                    EntityParamListType paramList = EntityParamListType.Factory.newInstance();
                    myMaker.addParameters(paramList, child.getParams());
                    if (paramList.getParamArray().length > 0) {
                        childService.setParamList(paramList);
                    }
                    /*
                    * Association list
                    */
                    AssocListType assocList = associationMaker.createAssociationsFromOrderDataAssociations(child.getAssociations());
                    if (assocList.getAssociationArray().length > 0) {
                        childService.setAssociationList(assocList);
                    }
                }
            }
        }

        // todo: THIS MUST BE A BUG !!!!!!!
        private static void deleteSubscription(ActionOrderValue.OrderItemList orderItemList
            , OrderData servicePlan, String kundeId) {

            OrderItemType servicePart = orderItemList.addNewOrderItem();
            servicePart.setAction(samf.toAction(servicePlan.getAction()).getValue());

            SubType sSubType = SubType.Factory.newInstance();
            sSubType.setServiceProvider("YouSee");
            sSubType.setSubscriberType("residential");
            sSubType.setLocale("en_US");
            EntityKeyType entityKey = sSubType.addNewKey();

            SubKeyType entKey = SubKeyType.Factory.newInstance();
            entKey.setExternalKey(servicePlan.getExternalKey());
            entKey.setType("SubSpec:-");
            entityKey.set(entKey);
            sSubType.setState("deleted");
            EntityParamListType eParamList = sSubType.addNewParamList();
            ParamType parameter = eParamList.addNewParam();
            parameter.setName("acct");
            parameter.setStringValue(kundeId);
            servicePart.setEntityKey(entKey);
            //Add service to request
            EntityListType entityList = sSubType.addNewEntityList();
            for (OrderData child : servicePlan.getChildren()) {

                if (child.getLevel() == OrderDataLevel.ADDRESS) {
                    logger.debug("Adding address");
                    EntityValue entVal = entityList.addNewEntityValue();
                    entVal.set(createAddressEntityValue(child));
                }
                // Create contact_on_address
                if (child.getLevel() == OrderDataLevel.CONTACT) {
                    logger.debug("Adding contact");
                    EntityValue entVal = entityList.addNewEntityValue();
                    entVal.set(createContactEntityValue(child));
                }
                if (child.getLevel() == OrderDataLevel.SERVICE) {
                    logger.debug("Adding samp sub");
                    EntityValue entVal = entityList.addNewEntityValue();
                    entVal.set(createSampSubEntityValue(kundeId));
                }
            }
            servicePart.setEntityValue(sSubType);
        }
        private static SubAddressKeyType createAddressKey() {
            SubAddressKeyType subAddressKeyType = SubAddressKeyType.Factory.newInstance();
            subAddressKeyType.setType("SubAddressSpec:-");
            subAddressKeyType.setExternalKey("primary");
            return subAddressKeyType;
        }

        private static SubAddressType createAddressEntityValue(OrderData addressData) {
            SubAddressKeyType subAddressKeyType = createAddressKey();
            SubAddressType addressEntity = SubAddressType.Factory.newInstance();
            addressEntity.setKey(subAddressKeyType);
            addressEntity.setState(sstc.toSimpleState(addressData.getAction()).getStringValue());
            addressEntity.setAddressType("service");
            addressEntity.setIsDefault(true);
            EntityParamListType addressParamList = addressEntity.addNewParamList();
            myMaker.addParameters(addressParamList, new HashMap<String, String>());
            return addressEntity;
        }

        private static SubSvcType createSampSubEntityValue(String kundeId) {
            SubSvcType entity = SubSvcType.Factory.newInstance();

            entity.xsetServiceState(sstc.toState(Action.DELETE));

            SubSvcKeyType subKeyType = createSampSubKey(kundeId);
            entity.setServiceKey(subKeyType);
            ParamType paramType = entity.addNewParamList().addNewParam();
            paramType.setName("acct");
            paramType.setStringValue(kundeId);

            AssocListType associations = entity.addNewAssociationList();
            AssocType addressAddressAssoc = associations.addNewAssociation();
            addressAddressAssoc.setAssociationType("service_on_address");
            SubAddressKeyType zEndKey = SubAddressKeyType.Factory.newInstance();
            zEndKey.setType("SubAddressSpec:-");
            zEndKey.setExternalKey("primary");
            addressAddressAssoc.setZEndKey(zEndKey);
            return entity;
        }
        private static SubSvcKeyType createSampSubKey(String kundeId) {
            SubSvcKeyType subKeyType = SubSvcKeyType.Factory.newInstance();
            subKeyType.setType("SubSvcSpec:samp_sub");
            subKeyType.setExternalKey("sigma_samp_sub_" + kundeId);
            return subKeyType;
        }

        private static SubContactType createContactEntityValue(OrderData contactData) {
            SubContactType contactType = SubContactType.Factory.newInstance();
            SubContactKeyType subContactKey = createContactKey();
            contactType.setKey(subContactKey);
            contactType.setState(sstc.toSimpleState(contactData.getAction()).getStringValue());
            EntityParamListType addressParamList = contactType.addNewParamList();
            myMaker.addParameters(addressParamList, new HashMap<String, String>());
            AssocListType associations = contactType.addNewAssociationList();
            AssocType contactAddressAssoc = associations.addNewAssociation();
            contactAddressAssoc.setAssociationType("contact_on_address");
            SubAddressKeyType zEndKey = SubAddressKeyType.Factory.newInstance();
            zEndKey.setType("SubAddressSpec:-");
            zEndKey.setExternalKey("primary");
            contactAddressAssoc.setZEndKey(zEndKey);
            return contactType;
        }

        private static SubContactKeyType createContactKey() {
            SubContactKeyType subContactKey = SubContactKeyType.Factory.newInstance();
            subContactKey.setType("SubContactSpec:-");
            subContactKey.setExternalKey("primary");
            return subContactKey;
        }

    }

    @Override
    protected ExecuteOrderReply convertResponse(SmpXml xml, Order order) {
        return new Parser().convertResponse(xml);
    }

    @Override
    public Integer getOperationTimeout() {
        return 100000;
    }

    private static class Parser extends OrderParser<ExecuteOrderReply> {

        @Override
        public ExecuteOrderReply convertResponse(SmpXml xml) {
            String responseXml=xml.getResponse();
            XmlObject xmlObject = parseResponse(responseXml);

            XmlObject[] res = xmlObject.selectPath("declare namespace smpsa='http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema'; $this//smpsa:executeOrderException");
            if (res.length > 0) {
                // This is an error
                String errorMessage;
                ExecuteOrderExceptionDocument.ExecuteOrderException ex = (ExecuteOrderExceptionDocument.ExecuteOrderException) res[0];
                if (ex.getRemoteException() != null){
                    errorMessage=ex.getRemoteException().getMessage();
                }
                else if (ex.getObjectNotFoundException() != null){
                    errorMessage=ex.getObjectNotFoundException().getMessage();
                }
                else if (ex.getIllegalArgumentException() != null) {
                    errorMessage=ex.getIllegalArgumentException().getMessage();
                }
                else {
                    errorMessage=null;
                }
                return new ExecuteOrderReply(errorMessage,xml);

            } else {
                res = xmlObject.selectPath("declare namespace smpsa='http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema'; $this//smpsa:executeOrderResponse");
                if (res.length > 0) {
                    // Everything aparantly went fine
                    logger.debug("Got ExecuteOrderResponse back!");
                    ExecuteOrderResponseDocument.ExecuteOrderResponse resp = (ExecuteOrderResponseDocument.ExecuteOrderResponse) res[0];
                    ExecuteOrderReply.MadeOrder eor = parseMadeOrder(resp);
                    return new ExecuteOrderReply(eor,xml);
                } else {
                    return new ExecuteOrderReply("Error in parsing result, no order",xml);
                }
            }
        }

    }
}
