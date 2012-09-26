package dk.yousee.smp.com;

import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ExecuteOrderResponseDocument;
import dk.yousee.smp.order.model.ExecuteOrderReply;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 03/06/12
 * Time: 11.09
 * Ability to pass results with an order number from SMP
 */
public abstract class OrderParser<OUTPUT> extends ResponseParser<OUTPUT> {

    //    static String apiClientId = "triple";
    private static final ServiceStateTypeConverter sstc = new ServiceStateTypeConverter();

    ExecuteOrderReply.MadeOrder parseMadeOrder(ExecuteOrderResponseDocument.ExecuteOrderResponse smpResponse){
        ExecuteOrderReply.MadeOrder reply;
        String orderId =smpResponse.getOrderKey().getPrimaryKey();
        Integer oid=Integer.valueOf(orderId);
        reply=new ExecuteOrderReply.MadeOrder(oid,sstc.find(smpResponse.getOrderState().getSmpState()));

//        dataItem.setState(sstc.find(smpResponse.getOrderState().getSmpState()));
//        dataItem.setType(OrderDataType.create(smpResponse.getOrderKey().getType()));
//        dataItem.setExternalKey(orderId);
//        OrderResult result = new OrderResult();
//        result.addOrderData(dataItem);
//        result.setType(OrderResult.TYPE_PROVISIONING);
//        List<OrderResult> results = new ArrayList<OrderResult>();
//        results.add(result);
//        Integer oid=Integer.valueOf(orderId);
//        OrderReply reply;
//        reply = new OrderReply(oid,results);

        return reply;
    }

}
