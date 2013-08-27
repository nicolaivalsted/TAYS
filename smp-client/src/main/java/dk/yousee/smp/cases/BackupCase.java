package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.backup.Backup;
import dk.yousee.smp.casemodel.vo.backup.BackupService;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;

import java.util.ArrayList;
import java.util.List;

/**
 * Case for backup update in SMP
 */
public class BackupCase extends AbstractCase {


    public BackupCase(OrderService service, Acct acct) {
        super(acct, service);
    }

    /**
     * Construct this case based on existing Subscriber Case<br/>
     * This is a kind of chaining of use-cases. <br/>
     *
     * @param customerCase subscriber case
     * @param keepModel    true to use the model from subscriber case, false to start a new model (default originally false)
     */
    public BackupCase(SubscriberCase customerCase, boolean keepModel) {
        super(selectModel(customerCase.getModel(), keepModel), customerCase.getService());
    }

    /**
     * Inner class that holds the contract between CRM and SMP
     */
    public static class BackupData {

        private BusinessPosition businessPosition;
        private String ysproPcode = "4020";
        private String uuid;
        private String licenseType;
        private String modemId;


        public BackupData() {
        }

        public BackupData(String uuid) {
            this.uuid = uuid;
        }

        public BusinessPosition getBusinessPosition() {
            return businessPosition;
        }

        public void setBusinessPosition(BusinessPosition businessPosition) {
            this.businessPosition = businessPosition;
        }

        public String getYsproPcode() {
            return ysproPcode;
        }

        public String getUuid() {
            return uuid;
        }

        public String getLicenseType() {
            return licenseType;
        }

        public void setLicenseType(String licenseType) {
            this.licenseType = licenseType;
        }

        public ModemId getModemId() {
            return ModemId.create(modemId);
        }

        public void setModemId(ModemId modemId) {
            this.modemId = modemId == null ? null : modemId.getId();
        }
    }

    /**
     * Create Backup
     *
     *
     * @param lineItem data to add
     * @return order for this
     * @throws dk.yousee.smp.order.model.BusinessException on error like no subscriber
     */
    public Order create(BackupData lineItem) throws BusinessException {
        ensureAcct();

        Backup def = getModel().alloc().Backup(lineItem.getBusinessPosition());
        def.license_type.setValue(lineItem.getLicenseType());

        ModemId modemId = lineItem.getModemId();
        def.modem_id.setValue(modemId == null ? null : modemId.getId());

        def.yspro_pcode.setValue(lineItem.getYsproPcode());

        return getModel().getOrder();
    }

    public BackupData readProvisioning(BusinessPosition position) throws BusinessException {
        BackupService backupService = getModel().find().BackupService(position);
        BackupData res;
        if (backupService == null) {
            res = null;
        } else {
            res = new BackupData(backupService.getBackup().yspro_provisioningid.getValue());
        }
        return res;
    }

    /**
     * Delete function
     *
     * @param position selected service plan instance
     * @return true if anything was marked for delete, false if nothing marked for delete.<br/>
     * Hereby the client can decide if anything needs to be send to Sigma
     * @throws dk.yousee.smp.order.model.BusinessException when <br/>
     *                                                     1) The customer does not exist<br/>
     */
    public boolean delete(BusinessPosition position) throws BusinessException {
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
        BackupService service = getModel().find().BackupService(position);
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
        return doAnything;
    }

    public class BackupActivationData {
        BusinessPosition position;
        String externalKey;

        public BackupActivationData(BusinessPosition position) {
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
    public List<BackupActivationData> readAll() throws BusinessException {
        ensureAcct();

        List<BackupActivationData> res = new ArrayList<BackupActivationData>();
        for (BackupService service : getModel().find().BackupService()) {
            BackupActivationData row = new BackupActivationData(service.getPosition());
            Backup backup = service.getBackup();
            if (backup != null) { // should never be null
                row.setUuid(backup.yspro_provisioningid.getValue());
                row.setExternalKey(backup.getExternalKey());
            } else {
                row.setUuid("error");
            }
            res.add(row);
        }
        return res;
    }

    public Order update(BusinessPosition position, BackupData data) throws BusinessException {
        ensureAcct();

        Backup backup = getModel().find().Backup(position);
        if (backup == null) {
            throw new BusinessException("Update failed, Backup service Plan was not found: for position: %s", position);
        }

        if(data.getBusinessPosition()!=null){
            backup.business_position.setValue(data.getBusinessPosition().getId());
        }

        return getModel().getOrder();
    }


}
