package dk.yousee.smp.com;

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
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.OrderInfo;
import dk.yousee.smp.order.model.OrderStateEnum;
import dk.yousee.smp.order.model.QueryOrdersBySubscriberReply;
import dk.yousee.smp.order.model.SmpXml;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 01/06/12
 * Time: 21.30
 * Search for customer
 */
public class FindOrdersBySubscriberCom extends SmpCom<Acct, QueryOrdersBySubscriberReply> {

    private boolean open;

    public FindOrdersBySubscriberCom(boolean open) {
        this.open = open;
    }

    @Override
    protected String convertRequest(Acct acct) {
        return new Pack().generateRequest(acct);
    }

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

    @Override
    public Integer getOperationTimeout() {
        return 4000;
    }

    private static final Logger logger = Logger.getLogger(FindOrdersBySubscriberCom.class);

    @Override
    protected QueryOrdersBySubscriberReply convertResponse(SmpXml xml, Acct acct) {
        return new Parser(open).convertResponse(xml);
    }

    private static final Set closedSet = new HashSet<OrderStateEnum>() {{
        add(OrderStateEnum.CLOSED);
        add(OrderStateEnum.CLOSED_ABORTED);
        add(OrderStateEnum.CLOSED_COMPLETED);
        add(OrderStateEnum.CLOSED_ABORTED_BYCLIENT);
        add(OrderStateEnum.CLOSED_ABORTED_BYSERVER);
    }};

    static class Parser extends ResponseParser<QueryOrdersBySubscriberReply> {

        private boolean open;

        public Parser(boolean open) {
            this.open = open;
        }

        @Override
        public QueryOrdersBySubscriberReply convertResponse(SmpXml xml) {

            return parseResultForQueryOrdersBySubscriber(xml, open);
        }

        public QueryOrdersBySubscriberReply parseResultForQueryOrdersBySubscriber(SmpXml xml, boolean open) {

            XmlObject xmlObject = parseResponse(xml.getResponse());

            /* ERROR HANDLING */
            XmlObject[] res;// = xmlObject.selectPath("declare namespace smpce='http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema'; $this//smpce:getOrderByKeyException");

            //for queryOrdersBySubscriber errors
            res = xmlObject.selectPath("declare namespace smpce='http://java.sun.com/products/oss/xml/ServiceActivation'; $this//smpce:queryOrdersException");
            if (res.length > 0) {
                String errorMessage;
                // This is an error
                QueryOrdersExceptionDocument.QueryOrdersException ex = (QueryOrdersExceptionDocument.QueryOrdersException) res[0];
                if (ex.getRemoteException() != null){
                    errorMessage=ex.getRemoteException().getMessage();
                }
                else if (ex.getIllegalArgumentException() != null) {
                    errorMessage=ex.getIllegalArgumentException().getMessage();
                } else {
                    errorMessage=null;
                }
                return new QueryOrdersBySubscriberReply(errorMessage,xml);
            } else {
                /* PARSE RESULTS */
                //for queryOrdersBySubscriber
                res = xmlObject.selectPath("declare namespace sa='http://java.sun.com/products/oss/xml/ServiceActivation'; $this//sa:queryOrdersResponse");
                logger.debug("Select queryOrdersResponse res length: " + res.length);
                if (res.length > 0) {
                    QueryOrdersResponseDocument.QueryOrdersResponse entity = (QueryOrdersResponseDocument.QueryOrdersResponse) res[0];
                    List<OrderInfo> list=parseQueryOrdersBySubscriber(entity, open);
                    return new QueryOrdersBySubscriberReply(list,xml);
                } else {
                    return new QueryOrdersBySubscriberReply("expected queryOrdersResponse in response",xml);
                }
            }
        }

        List<OrderInfo> parseQueryOrdersBySubscriber(QueryOrdersResponseDocument.QueryOrdersResponse queryOrdersResponse, boolean open) {

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
