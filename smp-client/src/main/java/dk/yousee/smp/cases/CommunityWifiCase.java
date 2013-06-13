package dk.yousee.smp.cases;

import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
import dk.yousee.smp.casemodel.vo.cwifi.CommunityWifi;
import dk.yousee.smp.casemodel.vo.cwifi.CommunityWifiService;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;

/**
 * Case for communityWifi update in SMP
 */
public class CommunityWifiCase extends AbstractCase {


    public CommunityWifiCase(OrderService service, Acct acct) {
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
    public CommunityWifiCase(SubscriberCase customerCase, boolean keepModel) {
        super(selectModel(customerCase.getModel(),keepModel), customerCase.getService());
    }

    /**
     * Inner class that holds the contract between CRM and SMP
     */
    public static class CommunityWifiData {

        public CommunityWifiData() {
        }

        public CommunityWifiData(String uuid) {
            this.uuid = uuid;
        }

        private String ysproPcode="1200";

        public String getYsproPcode() {
            return ysproPcode;
        }

        private String uuid;

        public String getUuid() {
            return uuid;
        }
    }

    /**
     * Create CommunityWifi
     *
     * @param position where?
     * @param lineItem data to add
     * @return order for this
     * @throws dk.yousee.smp.order.model.BusinessException
     *          on error like no subscriber
     */
    public Order createProvisioning(BusinessPosition position, CommunityWifiData lineItem) throws BusinessException {
        ensureAcct();

        CommunityWifi def = getModel().alloc().CommunityWifi(position);

        def.yspro_pcode.setValue(lineItem.getYsproPcode());

        return getModel().getOrder();
    }

    public Order updateHsdAccess(BusinessPosition position) throws BusinessException {

        CommunityWifi cwifi = getModel().find().CommunityWifi(position);
        if (cwifi == null) {
        	throw new BusinessException("Community WiFi not found at position: " + position.getId());
        }

    	// Add this cwifi to existing hsdAccess
        HsdAccess hsdAccess = getModel().find().HsdAccess(new ModemId(position.getId()));
        if (hsdAccess != null) {
        	hsdAccess.community_wifi.add(cwifi);
        }
        
        return getModel().getOrder();
    	
    }
    
    public CommunityWifiData readProvisioning(BusinessPosition position) throws BusinessException {
        CommunityWifiService communityWifiService= getModel().find().CommunityWifiService(position);
        CommunityWifiData res;
        if (communityWifiService == null) {
            res = null;
        } else {
            res = new CommunityWifiData(communityWifiService.getCommunityWifi().yspro_provisioningid.getValue());
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
            CommunityWifiService service= getModel().find().CommunityWifiService(position);
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

        public class CommunityWifiActivationData {
        BusinessPosition position;
        String externalKey;

        public CommunityWifiActivationData(BusinessPosition position) {
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
    public List<CommunityWifiActivationData> readAll() throws BusinessException {
        ensureAcct();

        List<CommunityWifiActivationData> res = new ArrayList<CommunityWifiActivationData>();
        for (CommunityWifiService service : getModel().find().CommunityWifiService()) {
            CommunityWifiActivationData row = new CommunityWifiActivationData(service.getPosition());
            CommunityWifi communityWifi = service.getCommunityWifi();
            if (communityWifi != null) { // should never be null
                row.setUuid(communityWifi.yspro_provisioningid.getValue());
                row.setExternalKey(communityWifi.getExternalKey());
            } else {
                row.setUuid("error");
            }
            res.add(row);
        }
        return res;
    }

}
