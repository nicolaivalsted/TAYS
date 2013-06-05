package dk.yousee.smp.com;

import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ActionOrderValue;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyExceptionDocument;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyRequestDocument;
import com.sun.java.products.oss.xml.serviceActivation.GetOrderByKeyResponseDocument;
import com.sun.java.products.oss.xml.serviceActivation.OrderKey;
import com.sun.java.products.oss.xml.serviceActivation.OrderStateType;
import dk.yousee.smp.order.model.OrderStateEnum;
import dk.yousee.smp.order.model.QueryOrderReply;
import dk.yousee.smp.order.model.SmpXml;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 01/06/12
 * Time: 21.30
 * Find order in SMP
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
            String response=xml.getResponse();
            XmlObject xmlObject=parseResponse(response);

            /* ERROR HANDLING */
            //for queryOrdersByOrderNumber errors
            XmlObject[] res = xmlObject.selectPath("declare namespace smpce='http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema'; $this//smpce:getOrderByKeyException");
            if (res.length > 0) {
                String errorMessage;
                // This is an error
                GetOrderByKeyExceptionDocument.GetOrderByKeyException ex = (GetOrderByKeyExceptionDocument.GetOrderByKeyException) res[0];
                if (ex.getRemoteException() != null){
                    errorMessage=ex.getRemoteException().getMessage();
                } else if (ex.getObjectNotFoundException() != null) {
                    errorMessage=ex.getObjectNotFoundException().getMessage();
                } else if (ex.getIllegalArgumentException() != null) {
                    errorMessage=ex.getIllegalArgumentException().getMessage();
                } else {
                    errorMessage=null;
                }
                return new QueryOrderReply(errorMessage,xml);
            } else {
                /* PARSE RESULTS */
                //for queryOrdersByOrderNumber
                res = xmlObject.selectPath("declare namespace sa='http://java.sun.com/products/oss/xml/ServiceActivation'; $this//sa:getOrderByKeyResponse");
                logger.debug("Select getOrderByKeyResponse res length: " + res.length);
                if (res.length > 0) {
//                    logger.debug("GetOrderByKeyResponse");
                    GetOrderByKeyResponseDocument.GetOrderByKeyResponse entity = (GetOrderByKeyResponseDocument.GetOrderByKeyResponse) res[0];
                    OrderStateEnum orderStateEnum=selectOrderState(entity);    // OK this has value, the order status is xxx !!
//                    ResponseEntity smp=parseSmpRoot(entity);                  // ??? this parsing is wrong !!! (only little relevance)
                    return new QueryOrderReply(orderStateEnum,xml);
                } else {
                    return new QueryOrderReply("expected getOrderByKeyResponse",xml);
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

//        /**
//         * TODO: return releveance !!!! a
//         * @param entity response
//         * @return should be a head with order number,  "orderItemList".
//         * The list should consist of OrderItem
//         *
//         */
//        private ResponseEntity parseSmpRoot(final GetOrderByKeyResponseDocument.GetOrderByKeyResponse entity) {
//            ResponseEntity smp = new ResponseEntity();
//            smp.setLevel(ResponseEntityLevel.TOP);
//            List<ResponseEntity> almostServicePlans = new ArrayList<ResponseEntity>();
//            if (entity.getOrderValue() instanceof ActionOrderValue) {
//                ActionOrderValue actionOrder = (ActionOrderValue) entity.getOrderValue();
//                ActionOrderValue.OrderItemList itemList = actionOrder.getOrderItemList();
//                if (itemList != null && itemList.sizeOfOrderItemArray() > 0) {
//                    int j = 0;
//                    while (j < itemList.sizeOfOrderItemArray()) {
//                        OrderItemType item = itemList.getOrderItemArray()[j];
//                        if (item.getEntityValue() instanceof SubType) {
//                            SubType entitySubType = (SubType) item.getEntityValue();
//
//                            smp.setExternalKey(entitySubType.getKey().getExternalKey());
//                            EntityListType entListType = entitySubType.getEntityList();
//
//                            for (EntityValue entityValue : entListType.getEntityValueArray()) {
//                                ResponseEntity dataChild;
//                                dataChild = parseEntityValue(entityValue);
//                                if (dataChild != null) almostServicePlans.add(dataChild);
//                            }
//                        } else {
//                            EntityValue entityValue = (EntityValue) item.getEntityValue();
//                            ResponseEntity dataChild;
//                            dataChild = parseEntityValue(entityValue);
//                            if (dataChild != null) almostServicePlans.add(dataChild);
//                        }
//                        j++;
//                    }
//                }
//            }
//            smp.setEntities(almostServicePlans);
//            return smp;
//        }

//        private ResponseEntity parseEntityValue(EntityValue entVal) {
//            ResponseEntity res = new ResponseEntity();
////        logger.debug("EntVal is of class: " + entVal.getClass().toString());
//            if (entVal instanceof SubContactType) { // service activation schema
//                SubContactType subKey = (SubContactType) entVal;
//                res.setLevel(ResponseEntityLevel.CONTACT);
//                EntityKeyType entKey = subKey.getKey();
//                res.setValue(new OrderDataType(String.valueOf(entKey.getType())));
//                res.setState(sstc.find(subKey.getState()));
//                res.setExternalKey(entKey.getExternalKey());
//                EntityParamListType entParamList = subKey.getParamList();
//                res.setParams(parseParamList(entParamList));
////            logger.debug("Contactof type: " + entKey.getType());
//                res.setAssociations(parseAssociations(subKey.getAssociationList()));
//
//            } else if (entVal instanceof SubAddressType) {
//                SubAddressType subKey = (SubAddressType) entVal;
//                res.setLevel(ResponseEntityLevel.ADDRESS);
//                res.setState(sstc.find(subKey.getState()));
//                res.setValue(new OrderDataType(subKey.getKey().getType()));
//                res.setExternalKey(subKey.getKey().getExternalKey());
//                EntityParamListType entParamList = subKey.getParamList();
//                res.setParams(parseParamList(entParamList));
//                res.setAssociations(parseAssociations(subKey.getAssociationList()));
//
//            } else if (entVal instanceof SubSvcType) {
////            logger.debug("SubSvcType");
//                SubSvcType subKey = (SubSvcType) entVal;
//                res.setLevel(ResponseEntityLevel.SERVICE);
//                res.setState(sstc.find((SubSvcStateType) subKey.xgetServiceState()));
//                res.setValue(new OrderDataType(subKey.getServiceKey().getType()));
//                res.setExternalKey(subKey.getServiceKey().getExternalKey());
//                SubSvcKeyType parentKey = subKey.getParentServiceKey(); // link back to parent service
//                if (parentKey != null) {
//                    String parentExtKey = parentKey.getExternalKey();
//                    if (parentExtKey != null) {
//                        logger.debug("Parent service key");
////                    OrderData parentKeyData = OrderHelper.createService(null, parentExtKey, Constants.SERVICE_TYPE_PARENT_SERVICE_KEY);
//                        // returnData.addChild(parentKeyData);
//                    }
//                }
//                EntityParamListType entParamList = subKey.getParamList();
////            logger.debug("Subsvctype of type: " + res.getType() + " with key: " + res.getExternalKey());
//                res.setParams(parseParamList(entParamList));
//                res.setAssociations(parseAssociations(subKey.getAssociationList()));
//                SubSvcType.ChildServiceList childserviceList = subKey.getChildServiceList();
//                res.getEntities().addAll(parseChildServiceList(childserviceList));
//            } else {
//                logger.warn("Unknown entity value");
//            }
//            return (res);
//        }

//        private static final ServiceStateTypeConverter sstc = new ServiceStateTypeConverter();

//        private Map<String,String> parseParamList(EntityParamListType paramL) {
//            Map<String, String> params = new TreeMap<String, String>();
//            if (paramL != null) {
//                for (ParamType param : paramL.getParamArray()) {
//                    if (param.getStringValue() != null) {
//                        params.put(param.getName(), param.getStringValue());
//                    }
//                }
//            }
//            return params;
//        }

//        /**
//         * @param associationList xml element with associations
//         * @return a list of all associations, null if none
//         */
//        private List<ResponseAssociation> parseAssociations(AssocListType associationList) {
//            List<ResponseAssociation> res = null;
//            if (associationList != null && associationList.sizeOfAssociationArray() != 0) {
//                res = new ArrayList<ResponseAssociation>();
//                for (AssocType one : associationList.getAssociationArray()) {
//                    ResponseAssociation ra = new ResponseAssociation();
//                    ra.setAssociationType(one.getAssociationType());
//                    ManagedEntityKey zxx = one.getZEndKey();
//                    ra.setType(new OrderDataType(zxx.getType()));
//                    String oneTxt = one.toString();
//                    ra.setPrimaryKey(grepPrimaryKey(oneTxt));
//                    res.add(ra);
//                }
//            }
//            return res;
//        }

        private static String grepPrimaryKey(final String source) {
            final String key = "primaryKey>";
            int pos0 = source.indexOf(key);
            if (pos0 == -1) return null;
            pos0 = pos0 + key.length();
            int pos1 = source.indexOf("<", pos0);
            if (pos1 == -1) return null;
            return source.substring(pos0, pos1);
        }

//        private List<ResponseEntity> parseChildServiceList(SubSvcType.ChildServiceList childserviceList) {
//            List<ResponseEntity> dataList = new ArrayList<ResponseEntity>();
//            if (childserviceList != null) {
//                for (SubSvcType s : childserviceList.getServiceValueArray()) {
//                    ResponseEntity dataItem;
//                    dataItem = new ResponseEntity();
//                    dataItem.setLevel(ResponseEntityLevel.CHILD_SERVICE);
//                    dataItem.setValue(new OrderDataType(s.getServiceKey().getType()));
////                logger.debug("ChildService of type: " + dataItem.getType());
//                    dataItem.setState(sstc.find((SubSvcStateType) s.xgetServiceState()));
//                    dataItem.setExternalKey(s.getServiceKey().getExternalKey());
//                    dataItem.setParams(parseParamList(s.getParamList()));
//
//                    dataItem.setAssociations(parseAssociations(s.getAssociationList()));
//
//                    SubSvcKeyType parentKey = s.getParentServiceKey(); // link back to parent service
//                    if (parentKey != null) {
//                        String parentExtKey = parentKey.getExternalKey();
//                        if (parentExtKey != null) {
////                        logger.debug("Parent service key");
////                        OrderData parentKeyData = OrderHelper.createService(null, parentExtKey, Constants.SERVICE_TYPE_PARENT_SERVICE_KEY);
////                        dataItem.addChild(parentKeyData);
//                        }
//                    }
//                    dataList.add(dataItem);
//                }
//            }
//            return dataList;
//        }
    }

    /**
     * What would be relevant to return back from this ....
     * todo: Make this content..
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
