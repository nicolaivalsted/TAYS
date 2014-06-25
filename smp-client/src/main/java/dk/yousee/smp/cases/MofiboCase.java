package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.mofibo.Mofibo;
import dk.yousee.smp.casemodel.vo.mofibo.MofiboService;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;

/**
 *
 * @author simonk
 */
public class MofiboCase extends AbstractCase {
    
    public MofiboCase(OrderService service, Acct acct) {
        super(acct, service);
    }

    /**
     * Construct this case based on existing Subscriber Case<br/>
     * This is a kind of chaining of use-cases. <br/>
     *
     * @param customerCase subscriber case
     * @param keepModel true to use the model from subscriber case, false to start a new model (default originally false)
     *
     */
    public MofiboCase(SubscriberCase customerCase, boolean keepModel) {
        super(selectModel(customerCase.getModel(),keepModel), customerCase.getService());
    }
    
    /**
     * Create Mofibo
     *
     * @param position where?
     * @return order for this
     * @throws dk.yousee.smp.order.model.BusinessException
     *          on error like no subscriber
     */
    public Order createProvisioning(BusinessPosition position) throws BusinessException {
        ensureAcct();

        Mofibo def = getModel().alloc().Mofibo(position);

        def.mofibo_pcode.setValue(Mofibo.MOFIBO_PRODUCT);
        return getModel().getOrder();
    }

    /**
     * Delete function
     *
     * @param position selected service plan instance
     * @return true if anything was marked for delete, false if nothing marked for delete.<br/>
     *         Hereby the client can decide if anything needs to be send to Sigma
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when <br/>
     *          1) The customer does not exist<br/>
     */
    public boolean delete(BusinessPosition position)
        throws BusinessException {
        ensureAcct();
        boolean res;
        res = buildOrderFromAction(position, Action.DELETE);
        return res;
    }


    /**
     * Constructs an order from action change
     *
     * @param position selected service plan instance (key is modemId)
     * @param action   the action to send to the subscription
     * @return true if anything to do
     */
    private boolean buildOrderFromAction(BusinessPosition position, Action action) {
        boolean doAnything = false;
        {
            MofiboService service= getModel().find().mofiboService(position);
            if (service != null) {
                doAnything = true;

                /**
                 * It was proven from tests that delete on top level works.<br/>
                 * But suspend/resume must be performed at each child-service
                 * <p>
                 * Tests shows that marking elements for something the element already is results in no order line
                 * in Sigma. This might be use full when sending commands to Sigma - so sending too much does not
                 * really matter in Sigma !!
                 * </p>
                 *
                 */
                if (action == Action.DELETE) {
                    service.sendAction(action);
                } else {
                    service.cascadeSendAction(action);
                }
            }
        }
        return doAnything;
    }
}
