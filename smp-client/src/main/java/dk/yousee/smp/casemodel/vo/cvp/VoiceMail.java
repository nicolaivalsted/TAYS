package dk.yousee.smp.casemodel.vo.cvp;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.PhoneNumber;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 21, 2010
 * Time: 3:02:38 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.4.1.3	Voice Mail Service
 */
public class VoiceMail extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType( ServicePrefix.SubSvcSpec,"primary_voicemail_box");

    public VoiceMail(SubscriberModel model, String externalKey, CableVoiceService parent) {
        super(model, externalKey, TYPE, LEVEL, parent);
        parent.setVoiceMail(this);
        voicemail_service_id.updateValue(externalKey);
    }

    //Type.FEATURE
    public PropHolder voicemail_service_id = new PropHolder(this, "voicemail_service_id", true);

    private PropHolder telephone_number = new PropHolder(this, "telephone_number", true);

    public PhoneNumber getPhoneNumber() {
        return PhoneNumber.create(telephone_number.getValue());
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        if(phoneNumber==null)throw new IllegalArgumentException("Phone number can never be null for VoiceMail");
        telephone_number.setValue(phoneNumber.getPhoneNumber());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(" telephone_number=").append(getPhoneNumber());
        sb.append('}');
        return sb.toString();
    }
}
