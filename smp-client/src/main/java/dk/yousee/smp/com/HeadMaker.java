package dk.yousee.smp.com;

import com.sigmaSystems.schemas.x31.smpCommonValues.ParamListType;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamType;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.OrderKeyType;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.SubOrderStateType;
import com.sun.java.products.oss.xml.serviceActivation.OrderStateType;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.util.OrderHelper;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 03/06/12
 * Time: 11.29
 * Makes things relevant for "head" level of xml document
 */
public class HeadMaker extends XmlMaker {

    /**
     * Will add orderParamList to the order
     * @param orderParamList collection to add to. Precondtion: N elements, Postcomdition: N+M elements (M={0..n})
     * @param order to take data from input
     */
    void updateOrderParams(ParamListType orderParamList, Order order) {
        ParamType parmTyp = orderParamList.addNewParam();
        parmTyp.setName("queue_request_flag");
        parmTyp.setStringValue("yes");
        parmTyp = orderParamList.addNewParam();
        parmTyp.setName("has_groups");
        parmTyp.setStringValue("yes");
        addParameters(orderParamList, order.getParams());

        if ((order.getExecDate() != null)){
            parmTyp = orderParamList.addNewParam();
            parmTyp.setName("start_datetime");
            parmTyp.setStringValue(OrderHelper.generateOrderDateString(order.getExecDate()));
        }

    }
    /**
     * Adds all params to the list
     *
     * @param paramList an list on input, on output this list has between 0 and N more elements
     * @param params    list of parameters to orderData / order
     */
    private void addParameters(com.sigmaSystems.schemas.x31.smpCommonValues.ParamListType paramList, Map<String, String> params) {
        for (String key : params.keySet()) {
            ParamType paramEnt;
            paramEnt = paramList.addNewParam();
            paramEnt.setName(key);
            paramEnt.setStringValue(params.get(key));
        }
    }

    public OrderKeyType createOrderKey(Integer orderId) {
        OrderKeyType ordKey = OrderKeyType.Factory.newInstance();
        ordKey.setType("com.sigma.samp.cmn.order.SampOrderKey");
        ordKey.setPrimaryKey(orderId==null?"":orderId.toString());
        return ordKey;
    }

    public SubOrderStateType createOrderStateType(){
        SubOrderStateType orderState = SubOrderStateType.Factory.newInstance();
        orderState.setSmpState(OrderStateType.OPEN_NOT_RUNNING_NOT_STARTED.toString());
        orderState.setStringValue(OrderStateType.OPEN_NOT_RUNNING_NOT_STARTED.toString());
        return orderState;
    }

}
