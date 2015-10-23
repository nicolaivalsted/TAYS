/**
 * 
 */
package dk.yousee.smp5.com;

import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.ExecuteOrderResponseDocument;

import dk.yousee.smp5.com.ResponseParser;
import dk.yousee.smp5.com.ServiceStateTypeConverter;
import dk.yousee.smp5.order.model.ExecuteOrderReply;

/**
 * @author m64746
 *
 * Date: 16/10/2015
 * Time: 13:13:32
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
