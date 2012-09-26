package dk.yousee.smp.casemodel.vo.mbs;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.helpers.AssociationHolder;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.Constants;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 21, 2010
 * Time: 2:31:13 PM<br/>
 * Data structure reference to YouSee Data Migration Requirements: 5.7.1	SMP Mobile Broadband Service Definition
 */
public class SMPMobileBroadbandDEF extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = Constants.SERVICE_TYPE_MOBB_SERVICE_DEF;

    public SMPMobileBroadbandDEF(SubscriberModel model, String externalKey, MobileBBService parent) {
        super(model, externalKey, TYPE, LEVEL, parent);
        parent.setSmpMobileBroadbandDEF(this);
        mobilebb_service_id.updateValue(externalKey);
    }

    //Type.FEATURE
    public PropHolder mobilebb_service_id = new PropHolder(this, "mobilebb_service_id", true);
    public PropHolder mobilebb_product_code = new PropHolder(this, "mobilebb_product_code", true);
    public PropHolder mobilebb_msisdn = new PropHolder(this, "mobilebb_msisdn", true);

    protected PropHolder tdc_subscriber_id = new PropHolder(this, "tdc_subscriber_id", true);

    public static final String MOBB_SUBSCRIBER_PREFIX = "446327224 ";

    /**
     * There is a bug in MOBB tech cartridge that makes this sub service without correct tdc_subscriber_id
     * <p>
     * Calling this method causes verifies the TDC subscriber
     * It will cause update of service plan when TDC subscriber is missing or wrong.
     * It is advised to log warning in such cases.
     * </p>
     * <p>
     * Note that updating needs to be executed as SMP update, cannot be part of SMP delete
     * The problem is that "delete" cannot do this update concurrently with deleting.
     *
     * Therefore only update will fix the value.
     * A fix in Provisioning would be to perform a MOBB_FIX_SUBSCRIBER command/action
     * and nest a delete command.
     *
     * This takes so much logic that it might be better to fix the root-cause of the problem.
     * The root cause is on SMP the flow for adding mobile-broad-band can fail.
     * When it fails - the following manual task - fails to update "tdc_subscriber_id" because a flow
     * flag is set to "false" and not "true". It would cause more deep analysis of this SMP flow to
     * understand the impact.
     * The case is spiced with situations where a MSISDN is in a wrong state at the service-portal.
     * </p>
     *
     * @return true when update was necessary
     */
    public boolean ensureSubscriberId() {
        boolean res;
        if (!subscriberIdIsValid()) {
            tdc_subscriber_id.setValue(MOBB_SUBSCRIBER_PREFIX + mobilebb_msisdn.getValue());
            res = true;
        } else {
            res = false;
        }
        return res;
    }

    /**
     * Verify that subscriber is valid
     * @return true means valid
     */
    public boolean subscriberIdIsValid(){
        boolean res;
        String current = tdc_subscriber_id.getValue();
        res = current != null && current.startsWith(MOBB_SUBSCRIBER_PREFIX);
        return res;
    }

//    public PropHolder TDC_Subscriber_ID = new PropHolder(this, "TDC_Subscriber_ID", true);     // todo: check the name
    /**
     * Field is used to force Sigma to update underlying systems.
     * <p>
     * The reasoning for this is that Sigma does not perform an order that -
     * in its matter does not change anything.
     * </p>
     */
    public TimeStampPropHolder mobilebb_time_stamp = new TimeStampPropHolder(this, "mobilebb_time_stamp");

    public PropHolder suspension_type_id = new PropHolder(this, "suspension_type_id");         // todo: check the name

    //Type.ASSOC
    public AssociationHolder mobile_service_defn_has_sim = new AssociationHolder(this, "mobile_service_defn_has_sim", SMPSIMCard.TYPE);
}
