package dk.yousee.smp.casemodel.vo.cbp;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
import dk.yousee.smp.casemodel.vo.helpers.AssociationHolder;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Oct 12, 2010 Time: 3:49:28 PM
 * Value object for Internet Access Data structure reference to YouSee Data
 * Migration Requirements: 5.5.1 Internet Access
 */
public class InetAccess extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "internet_access");

    public InetAccess(SubscriberModel model, String externalKey, CableBBService parent) {
        super(model, externalKey, TYPE, LEVEL, null, parent);
        parent.setInetAccess(this);
        broadband_service_id.updateValue(externalKey);
    }

    /**
     * Name of field
     */
    public static final String BUSINESS_POSITION = "business_position";
    /**
     * identifier that identify the subscribers modem among all the modems the
     * subscriber has. Field value can be "1", "2" etc. It is only required to
     * be unique for the subscriber. So two different subscribers can both have
     * position called "1" The objective is to manage relation to CRM
     * subscription. This is an instance key to service plan. It is normally
     * never modified. YouSee will fill in modem_id / aftale Nr / ... tbd.
     */
    public PropHolder business_position = new PropHolder(this, BUSINESS_POSITION);

    public BusinessPosition getPosition() {
        return BusinessPosition.create(business_position.getValue());
    }
    /**
     * Name of field
     */
    public static final String MODEM_ACTIVATION_CODE = "modem_activation_code";
    /**
     * Pin code that customer must enter to activate modem. For YouSee this is
     * the old classic "modem_id". For whole sale this value is generated in
     * Gaia.
     */
    protected PropHolder modem_activation_code = new PropHolder(this, MODEM_ACTIVATION_CODE);

    /**
     * Identifier for BACC account, and reference for VOICE media gateway
     * Request for better name !!!!
     */
    private PropHolder modem_id = new PropHolder(this, "modem_id");

    /**
     * Reads the assigned modemId from field modem_id or from external key if
     * modem_id is not assigned yet
     *
     * @return modemId
     */
    public ModemId getModemId() {
        if (modem_id.hasValue()) {
            return ModemId.create(modem_id.getValue());
        } else {
            return getParent().getModemIdOld();
        }
    }

    /**
     * Assign modemId to servicePlan
     *
     * @param modemId in cannot be null
     */
    public void setModemId(ModemId modemId) {
        if (modemId == null) {
            throw new IllegalArgumentException("ModemId can never be null on InetAccess, it is the primary key to BB-service");
        } else {
            modem_id.setValue(modemId.getId());
        }
    }

    /**
     * Name of field
     */
    public static final String RATE_CODES = "rate_codes";
    /**
     * Reference to rate for modem. Key to rate table in SMP
     */
    public PropHolder rate_codes = new PropHolder(this, RATE_CODES, true);
    /**
     * Internal reference to traverse the service plan
     */
    public PropHolder broadband_service_id = new PropHolder(this, "broadband_service_id", true);

    public PropHolder svc_provider_nm = new PropHolder(this, "svc_provider_nm");

    public PropHolder upstream_speed = new PropHolder(this, "upstream_speed");
    public PropHolder config_file_override = new PropHolder(this, "config_file_override");
    public PropHolder bill_ack = new PropHolder(this, "bill_ack");
    public PropHolder bottom_up_provisioned = new PropHolder(this, "bottom_up_provisioned");
    public PropHolder num_of_ips = new PropHolder(this, "num_of_ips");
    public PropHolder gi_address = new PropHolder(this, "gi_address");
    public PropHolder aup = new PropHolder(this, "aup");
    public PropHolder downstream_speed = new PropHolder(this, "downstream_speed");
    public PropHolder email_cos = new PropHolder(this, "email_cos");
    public PropHolder cm_mac = new PropHolder(this, "cm_mac");
    public PropHolder product_name = new PropHolder(this, "product_name");
    public PropHolder upstream_bonding_enabled = new PropHolder(this, "upstream_bonding_enabled");
    public PropHolder upstream_channel_bonding = new PropHolder(this, "upstream_channel_bonding");
    public PropHolder cisco_sm_package_id = new PropHolder(this, "cisco_sm_package_id");
    public PropHolder downstream = new PropHolder(this, "downstream_speed");
    public PropHolder upstream = new PropHolder(this, "upstream_speed");

    /**
     * Method to return activation code, will return modemId if property is
     * missing
     * <p>
     * BB's from Casper / Kasia uses modemId as self activation code.<br/>
     * BB from M5 uses a PIN code like activation code that has nothing to do
     * with modemId From ASU this tweak is hidden by this method.< </p> <p>
     * This method will return modem_activation_code from property if it exists
     * Otherwise it will return modemId
     * </p>
     * ASU can use this one to validate users / authentication in stead of
     * assuming it is modemId
     *
     * @return modem activation code. (PIN code)
     */
    public String getModemActivationCode() {
        if (modem_activation_code.hasValue()) {
            return modem_activation_code.getValue();
        } else {
            return getParent().getModemId().toString();
        }
    }

    /**
     * Assign activation code
     *
     * @param modemActivationCode to become PIN - null values are ignored (there
     * fore it legal to send a null) Null will be the case in the intermediate
     * faces before all software uses modemActivationCode to express this
     * situation
     */
    public void setModemActivationCode(String modemActivationCode) {
        if (modemActivationCode != null) {
            modem_activation_code.setValue(modemActivationCode);
        }
    }

    /**
     * intermediate question does this BB use modem_activation_code ?
     *
     * @return true means yes
     */
    public boolean isModemActivationCodeUsed() {
        return modem_activation_code.hasValue();
    }

    public CableBBService getParent() {
        return (CableBBService) super.getParent();
    }

    //Type.ASSOC
    public AssociationHolder internet_access_has_emta_cm = new AssociationHolder(this, "internet_access_has_emta_cm", HsdAccess.TYPE);

}
