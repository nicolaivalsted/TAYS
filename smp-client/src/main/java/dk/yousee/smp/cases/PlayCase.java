package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.play.Play;
import dk.yousee.smp.casemodel.vo.play.PlayService;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;

import java.util.ArrayList;
import java.util.List;

/**
 * Case for play update in SMP
 */
public class PlayCase extends AbstractCase {


    public PlayCase(OrderService service, Acct acct) {
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
    public PlayCase(SubscriberCase customerCase, boolean keepModel) {
        super(selectModel(customerCase.getModel(),keepModel), customerCase.getService());
    }

    /**
     * Inner class that holds the contract between CRM and SMP
     */
    public static class PlayData {

        public PlayData() {
        }

        public PlayData(String uuid) {
            this.uuid = uuid;
        }

        private String ysproPcode="1000";

        public String getYsproPcode() {
            return ysproPcode;
        }

        private String uuid;

        public String getUuid() {
            return uuid;
        }
    }

    /**
     * Create Play
     *
     * @param position where?
     * @param lineItem data to add
     * @return order for this
     * @throws dk.yousee.smp.order.model.BusinessException
     *          on error like no subscriber
     */
    public Order createProvisioning(BusinessPosition position, PlayData lineItem) throws BusinessException {
        ensureAcct();

        Play def = getModel().alloc().Play(position);

        def.yspro_pcode.setValue(lineItem.getYsproPcode());
        return getModel().getOrder();
    }

    public PlayData readProvisioning(BusinessPosition position) throws BusinessException {
        PlayService playService= getModel().find().PlayService(position);
        PlayData res;
        if (playService == null) {
            res = null;
        } else {
            res = new PlayData(playService.getPlay().yspro_provisioningid.getValue());
        }
        return res;
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
            PlayService service= getModel().find().PlayService(position);
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

        public class PlayActivationData {
        BusinessPosition position;
        String externalKey;

        public PlayActivationData(BusinessPosition position) {
            this.position = position;
        }

        public BusinessPosition getPosition() {
            return position;
        }

        public String getExternalKey() {
            return externalKey;
        }

        public void setExternalKey(String externalKey) {
            this.externalKey = externalKey;
        }

        private String uuid;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{position=").append(position);
            if (getUuid() != null) sb.append(", uuid='").append(uuid).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Read all activation data for this subscriber
     *
     * @return list of activation data
     * @throws dk.yousee.smp.order.model.BusinessException when subscriber is missing
     */
    public List<PlayActivationData> readAll() throws BusinessException {
        ensureAcct();

        List<PlayActivationData> res = new ArrayList<PlayActivationData>();
        for (PlayService service : getModel().find().PlayService()) {
            PlayActivationData row = new PlayActivationData(service.getPosition());
            Play play = service.getPlay();
            if (play != null) { // should never be null
                row.setUuid(play.yspro_provisioningid.getValue());
                row.setExternalKey(play.getExternalKey());
            } else {
                row.setUuid("error");
            }
            res.add(row);
        }
        return res;
    }

}
