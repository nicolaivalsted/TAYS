package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp.casemodel.vo.cpee.CpeComposedService;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 25, 2010
 * Time: 1:38:17 PM<br/>
 * Use case for global IP assignment
 */
public class GlobalIPCase extends AbstractCase {


    public GlobalIPCase(Acct acct, OrderService service) {
        super(acct, service);
    }

    /**
     * Construct this case based on existing Subscriber Case<br/>
     * This is a kind of chaining of use-cases. <br/>
     * <p>
     * First ask for the customer, eventually create him
     * Then work with global IP.
     * </p>
     *
     * @param customerCase subscriber case's
     */
    public GlobalIPCase(SubscriberCase customerCase) {
        super(customerCase.getModel(), customerCase.getService());
    }


    /**
     * Update GI-Address
     * @param modemId selected service plan instance (key is modemId)
     * @param gi_address the fixed IP address
     * @return true if anything to do
     * @throws dk.yousee.smp.order.model.BusinessException on err
     */
    public boolean updateGiAddress(ModemId modemId, String gi_address) throws BusinessException {
        boolean doAnything = false;
//        BssAdapterClient client = new BssAdapterClient(url);
//        // Set customer details for which we would like to query
//        Order order = new Order();
//        order.setType(Order.TYPE_QUERY);
//        order.setAsynchronous(false); // The order _is_ syncronous
//        order.setReturnEventUrl("http://localhost:6080/bss-adapter/devnull.service");
//        Subscriber subscriber = new Subscriber();
//        subscriber.setKundeId(customerNo);
//        order.setSubscriber(subscriber);
//        order.addOrderData(OrderHelper.createQuery(Constants.ACTION_FIND_CUSTOMER, null, null));
//        OrderReply reply = client.getOrderService().execute(order);
//        OrderData element = OrderHelper.findElementInReplyWithValue(reply, Constants.SERVICE_TYPE_CBB_EMTA_HAS_HSD);

        CpeComposedService servicePlan = getModel().find().CpeComposedService(modemId);//OrderHelper.findElementWithValue(element, Constants.SERVICE_TYPE_PARENT_SERVICE_KEY);
        if (servicePlan != null) {
//            String parentServiceKey = null;
//            parentServiceKey = servicePlan.getExternalKey();

            HsdAccess hsdAccess = getModel().find().HsdAccess(modemId);
            if (hsdAccess != null) {
                doAnything = true;
//                order = new Order();
//                order.setType(Order.TYPE_TECHNICAL_PROVISIONING);
//                order.setSubscriber(subscriber);
//                OrderData rootService = OrderHelper.createService(Constants.ACTION_UPDATE, "YouSee", Constants.SERVICE_TYPE_CBB);
//                order.addOrderData(rootService);
                getModel().getOrder().getParams().put("clientType", "migrated_sub");//
//                OrderData clientTypefeature = OrderHelper.createFeatureData(null, "clientType", "migrated_sub"); //TODO WHAT IS THIS ??
//                order.addOrderData(clientTypefeature);
//                OrderData emtaService = OrderHelper.createService(Constants.ACTION_UPDATE, hsdExtKey, Constants.SERVICE_TYPE_CBB_EMTA_HAS_HSD);
//                rootService.addChild(emtaService);
//                OrderData parentserviceKey = OrderHelper.createService(Constants.ACTION_UPDATE, parentServiceKey, Constants.SERVICE_TYPE_PARENT_SERVICE_KEY);
//                emtaService.addChild(parentserviceKey);
//                emtaService.addChild(OrderHelper.createFeatureData(null, "gi_address", Ip));
                hsdAccess.gi_address.setValue(gi_address);
                if (hsdAccess.service_on_address.get() == null) {
                    hsdAccess.service_on_address.add(getModel().find().SubAddressSpec());
                }
//                emtaService.addChild(OrderHelper.createAssocService(null, "primary", Constants.SERVICE_TYPE_ADDRESS_SPEC));
            }
        }
        return doAnything;
    }
}
