package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.mbs.MobileBBService;
import dk.yousee.smp.casemodel.vo.mbs.SMPMobileBroadbandAttributes;
import dk.yousee.smp.casemodel.vo.mbs.SMPMobileBroadbandDEF;
import dk.yousee.smp.casemodel.vo.mbs.SMPSIMCard;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;
import dk.yousee.smp.order.model.ProvisionStateEnum;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 25, 2010
 * Time: 1:38:17 PM<br/>
 * Use case for mobile broadband
 */
public class MobileBBCase extends AbstractCase {

    private static final Logger logger = Logger.getLogger(MobileBBCase.class);

    public MobileBBCase(Acct acct, OrderService service) {
        super(acct, service);
    }

    /**
     * Construct this case based on existing Subscriber Case<br/>
     * This is a kind of chaining of use-cases. <br/>
     * <p>
     * First ask for the customer, eventually create him
     * Then work with mobile broad band.
     * </p>
     *
     * @param customerCase subscriber case's
     */
    public MobileBBCase(SubscriberCase customerCase) {
        super(new SubscriberModel(customerCase.getModel().getResponse()), customerCase.getService());
    }

//    /**
//     * Find service plan for mobile broad band<br/>
//     * Use this call to see if the service plan exists before deleting it.
//     *
//     * @param modelId for the serviceplan
//     * @return service plan model, null if not exists
//     */
//    public MobileBBService findMobileBBServicePlan(ModemId modelId) {
//        return getModel().find().MobileBBService(modelId);
//    }

    /**
     * Return the customers first mobile broad band modem
     *
     * @return the first customer have
     */

    public ModemId firstModem() {
        List<MobileBBService> list = getModel().find().MobileBBService();
        if (list.isEmpty()) return null;
        MobileBBService first = list.get(0);
        return first.getModemId();

    }

    /**
     * Use-case 8: Erling: Lav en ordre, som indeholder en opret-service (lineItem.getVarenummer())<br/>
     * <p/>
     * Was previously called: mkCreateServiceOrder
     *
     * @param modemId  selected service plan instance (key is modemId)
     * @param lineItem data from casper / stalone
     * @return smpOrder - en Order(), som allerede har kunde opret/opdater udfyldt.
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when <br/>
     *          1) The customer does not exist<br/>
     */
    public Order createMoBB(ModemId modemId, AbonData lineItem) throws BusinessException {
//        Order smpOrder = new Order();
//        smpOrder.setType(Order.TYPE_TECHNICAL_PROVISIONING);
//
//
//        OrderData rootService = OrderHelper.createService(Action.ACTIVATE, "YouSee", Constants.SERVICE_TYPE_MOBB);
//        smpOrder.addOrderData(rootService);
//
//        OrderData serviceMobb = OrderHelper.createService(Action.ACTIVATE, "YouSee:mbb_" + lineItem.getModemID(), Constants.SERVICE_TYPE_MOBB);
//        rootService.addChild(serviceMobb);
        ensureAcct();

//        OrderData device = OrderHelper.createService(Action.ACTIVATE, "YouSee:sim_" + lineItem.getModemID(), Constants.SERVICE_TYPE_MOBB_SIM); // AUTOGEN?
//        rootService.addChild(device);
//        device.getParams().put( "sim_card_service_id", "YouSee:sim_" + lineItem.getModemID());
//        device.getParams().put( "sim_card_id", lineItem.getIcc());

        SMPSIMCard card = getModel().alloc().SMPSIMCard(modemId);
        card.sim_card_id.setValue(lineItem.getIcc());

        SMPMobileBroadbandDEF serviceDef = getModel().alloc().SMPMobileBroadbandDEF(modemId);
        serviceDef.mobilebb_msisdn.setValue(lineItem.getMsisdn());
        serviceDef.mobilebb_product_code.setValue(lineItem.getVarenummer());
        if (serviceDef.mobile_service_defn_has_sim.get() == null) {
            serviceDef.mobile_service_defn_has_sim.add(card);
        }

        getModel().add().SMPMobileBroadbandAttributes(modemId);
        //OrderHelper.createChildService(Action.UPDATE, "YouSee:" + UUIDGenerater.generateKey(), Constants.SERVICE_TYPE_MOBB_SERVICE_ATTRIBS);

//            serviceMobb.addChild(serviceAttribs);
//        serviceAttribs.mobilebb_service_attribs_id.setValue(serviceAttribs.getExternalKey());//getModel().key().generateUUID()); //AUTOGEN

//        OrderData serviceAttribs = OrderHelper.createChildService(Action.ACTIVATE, null, Constants.SERVICE_TYPE_MOBB_SERVICE_ATTRIBS);
//        serviceMobb.addChild(serviceAttribs);
//        serviceAttribs.getParams().put( "mobilebb_service_attribs_id", null); // AUTOGEN??

        return getModel().getOrder();
    }


    /**
     * <p>
     * Use-case 11: Erling: Lav en ordre, som indeholder en opdater-service (lineItem.getVarenummer()). smpService
     * indeholder den service (returneret fra smpQueryServicesByAcctModemId ovenfor),
     * som skal opdateres
     * </p>
     * Was previously called: mkUpdateServiceOrder
     * <p>
     * Does also implement the previous method called reprovisionMobb
     * </p>
     *
     * @param modemId     selected service plan instance (key is modemId)
     * @param lineItem    ordrelinien fra Casper, null fields are not updated !!
     * @param forceUpdate - setting this flag to true causes Sigma to perform the
     *                    order even that the parameters is identical.
     * @return smpOrder - en Order(), som allerede har kunde opret/opdater udfyldt.
     *         // TEST MOBB
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when <br/>
     *          1) The customer does not exist<br/>
     *          2) the Mobile Broad Band is not found for customer<br/>
     */
    public Order updateMoBB(ModemId modemId, AbonData lineItem, boolean forceUpdate) throws BusinessException {


        ensureAcct();
        MobileBBService serviceMobb = getModel().find().MobileBBService(modemId);
        if (serviceMobb == null) {
            throw new BusinessException(
                "Update failed,  MoBB service Plan was not found: for modemId: %s", modemId);
        }

        SMPSIMCard card = getModel().alloc().SMPSIMCard(modemId);
        if (lineItem.getIcc() != null) {
            card.sim_card_id.setValue(lineItem.getIcc());
        }

        SMPMobileBroadbandDEF serviceDef = getModel().alloc().SMPMobileBroadbandDEF(modemId);
        if (lineItem.getMsisdn() != null) {
            serviceDef.mobilebb_msisdn.setValue(lineItem.getMsisdn());
        }

        if (lineItem.getVarenummer() != null) {
            serviceDef.mobilebb_product_code.setValue(lineItem.getVarenummer());
        }
        if (serviceDef.mobile_service_defn_has_sim.get() == null) {
            serviceDef.mobile_service_defn_has_sim.add(card);
        }
        if (forceUpdate) {
            serviceDef.mobilebb_time_stamp.assignCurrentTime();
        }
        ensureSubscriberId(serviceMobb.getSmpMobileBroadbandDEF());


        SMPMobileBroadbandAttributes serviceAttribs = serviceMobb.first();
        if (serviceAttribs == null) {
            getModel().add().SMPMobileBroadbandAttributes(modemId);
        }


        return getModel().getOrder();
    }

    private void ensureSubscriberId(SMPMobileBroadbandDEF subService) {
        if (subService != null) {
            if (subService.ensureSubscriberId()) {
                logger.warn(String.format("SubscriberId was not assighed correct: %s", getAcct()));
            }
        }
    }

    /**
     * Inner class that holds the contract between mobile broad band - seen from Casper/ Stallone and Sigma
     */
    public static class AbonData {

        private String msisdn;
        private String varenummer;
        private String icc;

        /**
         * @return telephonenumber used for MoBB, null means it is not for update
         */
        public String getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
        }

        /**
         * @return product code used for MoBB, null means it is not for update
         */
        public String getVarenummer() {
            return varenummer;
        }

        public void setVarenummer(String varenummer) {
            this.varenummer = varenummer;
        }

        /**
         * @return sim card no used for MoBB, null means it is not for update
         */
        public String getIcc() {
            return icc;
        }

        public void setIcc(String icc) {
            this.icc = icc;
        }
    }

    /**
     * Use-case 10: Erling: Lav en ordre, som indeholder en Delete-service (lineItem.getVarenummer()).
     * smpService indeholder den service (returneret fra smpQueryServicesByAcctModemId ovenfor)
     * som skal slettes<br/>
     * Was called: mkDeleteServiceOrder
     *
     * @param modemId selected service plan instance (key is modemId)
     * @return true if anything was marked for delete, false if nothing marked for delete.<br/>
     *         Hereby the client can decide if anything needs to be send to Sigma
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when <br/>
     *          1) The customer does not exist<br/>
     */
    public boolean deleteMoBB(ModemId modemId)
        throws BusinessException {
        ensureAcct();
        return buildOrderFromAction(modemId, Action.DELETE);
    }

    /**
     * Suspend funktion
     *
     * @param modemId selected service plan instance (key is modemId)
     * @return true if anything was marked for suspension, false if nothing marked for suspension.<br/>
     *         Hereby the client can decide if anything needs to be send to Sigma
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when <br/>
     *          1) The customer does not exist<br/>
     */
    public boolean suspendMoBB(
        ModemId modemId
    ) throws BusinessException {
        ensureAcct();
        return buildOrderFromAction(modemId, Action.SUSPEND);
    }

    /**
     * resume funktion
     *
     * @param modemId selected service plan instance (key is modemId)
     * @return true if anything was marked for resume, false if nothing marked for resume.<br/>
     *         Hereby the client can decide if anything needs to be send to Sigma
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when <br/>
     *          1) The customer does not exist<br/>
     */
    public boolean resumeMoBB(
        ModemId modemId
    ) throws BusinessException {
        ensureAcct();
        return buildOrderFromAction(modemId, Action.RESUME);
    }

    /**
     * Constructs an order from action change
     *
     * @param modemId selected service plan instance (key is modemId)
     * @param action  the action to send to the subscription
     * @return true if anything to do
     */
    private boolean buildOrderFromAction(ModemId modemId, Action action) {
        boolean doAnything = false;


        MobileBBService serviceMobb = getModel().find().MobileBBService(modemId);

        if (serviceMobb != null) {
            doAnything = true;

            /**
             * It was proven from tests that delete on top level works.<br/> 
             * But suspend/resume must be performed at each child-service
             * <p>
             * Tests shows that marking elements for something the element already is results in no orderline
             * in Sigma. This might be use full when sending commands to Sigmna - so sending too much does not
             * really matter in Sigma !!
             * </p>
             *
             */
            if (action == Action.DELETE) {
                serviceMobb.sendAction(action);
            } else {
                serviceMobb.cascadeSendAction(action);
            }
        }
        SMPSIMCard card = getModel().find().SMPSIMCard(modemId);
        if (card != null) {
            doAnything = true;
            if (action == Action.DELETE) {
                card.sendAction(action);
            } else {
                card.cascadeSendAction(action);
            }
        }

        return doAnything;
    }

    /**
     * Report the MoBB
     *
     * @return a report of MoBB
     */
    public List<MobileBroadband> getMobileSubscriberDetails() {

        List<MobileBroadband> res = new ArrayList<MobileBroadband>();

        for (MobileBBService one : getModel().find().MobileBBService()) {
            ModemId modemId = one.getModemId();
            MobileBroadband row = new MobileBroadband(modemId);

            SMPMobileBroadbandDEF serviceDef = one.getSmpMobileBroadbandDEF();
            if (serviceDef != null) {
                row.setState(one.getEntity().getState());
                row.setMsisdn(serviceDef.mobilebb_msisdn.getValue());
                row.setVarenummer(serviceDef.mobilebb_product_code.getValue());
                row.setSuspensionTypeId(serviceDef.suspension_type_id.getValue());
            }
            SMPSIMCard card = getModel().find().SMPSIMCard(modemId);
            row.setIcc(card.sim_card_id.getValue());
            res.add(row);
        }
        return res;
    }


    public class MobileBroadband {
        private ModemId modemId;
        private String msisdn;
        private String varenummer;
        private String suspensionTypeId;
        private ProvisionStateEnum state;

        public ProvisionStateEnum getState() {
            return state;
        }

        public void setState(ProvisionStateEnum state) {
            this.state = state;
        }

        public String getVarenummer() {
            return varenummer;
        }

        public void setVarenummer(String varenummer) {
            this.varenummer = varenummer;
        }

        public ModemId getModemId() {
            return modemId;
        }

        private MobileBroadband(ModemId modemId) {
            this.modemId = modemId;
        }

        private String icc;

        public String getIcc() {
            return icc;
        }

        public void setIcc(String icc) {
            this.icc = icc;
        }

        public String getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
        }

        public String getSuspensionTypeId() {
            return suspensionTypeId;
        }

        public void setSuspensionTypeId(String suspensionTypeId) {
            this.suspensionTypeId = suspensionTypeId;
        }
    }
}
