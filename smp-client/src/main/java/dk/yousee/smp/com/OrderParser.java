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

    private static final ServiceStateTypeConverter sstc = new ServiceStateTypeConverter();

    ExecuteOrderReply.MadeOrder parseMadeOrder(ExecuteOrderResponseDocument.ExecuteOrderResponse smpResponse){
        ExecuteOrderReply.MadeOrder reply;
        String orderId =smpResponse.getOrderKey().getPrimaryKey();
        Integer oid=Integer.valueOf(orderId);
        reply=new ExecuteOrderReply.MadeOrder(oid,sstc.find(smpResponse.getOrderState().getSmpState()));

        return reply;
    }

}
