package dk.yousee.smp.casemodel.vo.cvp;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.PhoneNumber;
import dk.yousee.smp.casemodel.vo.cpee.VoipAccess;
import dk.yousee.smp.casemodel.vo.helpers.AssociationHolder;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 21, 2010
 * Time: 2:52:22 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.4.1.1	Dial Tone Access
 */
public class DialToneAccess extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType( ServicePrefix.SubSvcSpec,"smp_switch_dial_tone_access");

    public DialToneAccess(SubscriberModel model, String externalKey, CableVoiceService parent) {
        super(model, externalKey, TYPE, LEVEL,null, parent);
        parent.setDialToneAccess(this);
        dial_tone_service_id.updateValue(externalKey);
    }

    /**
     * Name of field
     */
    public static final String BUSINESS_POSITION = "business_position";
    /**
     * identifier that identify the subscribers modem among all the modems the subscriber has.
     * Field value can be "1", "2" etc. It is only required to be unique for the subscriber.
     * So two different subscribers can both have position called "1"
     * The objective is to manage relation to CRM subscription.
     * This is an instance key to service plan. It is normally never modified.
     * YouSee will fill in modem_id / aftale Nr / ... tbd.
     */
    protected PropHolder business_position = new PropHolder(this, BUSINESS_POSITION);

    public BusinessPosition getPosition() {
        return BusinessPosition.create(business_position.getValue());
    }
    public void setPosition(BusinessPosition businessPosition) {
        if(businessPosition==null){
            business_position.clearValue();
        } else {
            business_position.setValue(businessPosition.toString());
        }
    }
    
    public PropHolder dial_tone_service_id = new PropHolder(this, "dial_tone_service_id", true);

    private PropHolder telephone_number = new PropHolder(this, "telephone_number", true);

    public PhoneNumber getPhoneNumber() {
        return PhoneNumber.create(telephone_number.getValue());
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        if(phoneNumber==null)throw new IllegalArgumentException("Phone number can never be null for dial-tone-access");
        telephone_number.setValue(phoneNumber.getPhoneNumber());
    }

    public PropHolder mta_voice_port = new PropHolder(this, "mta_voice_port", true);
    public PropHolder rate_codes = new PropHolder(this, "rate_codes", true);
    public PropHolder Privacy = new PropHolder(this, "privacy", true);
    public PropHolder Cos_restrict_id = new PropHolder(this, "cos_restrict_id", true);
    
    public PropHolder cnam = new PropHolder(this, "cnam", true);
    
    public PropHolder mta_mac = new PropHolder(this, "mta_mac");
    
    //Type.ASSOC
    public AssociationHolder dt_has_access = new AssociationHolder(this, "dt_has_access", VoipAccess.TYPE );
//    public AssociationHolder service_on_address = new AssociationHolder(this, "service_on_address", SubAddressSpec.TYPE );
}
