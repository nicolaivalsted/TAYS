package dk.yousee.smp.casemodel.vo.cvp;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.PhoneNumber;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 21, 2010
 * Time: 2:46:20 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.4	Cable Voice Services Definition
 */
public class CableVoiceService extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"smp_voice_line");
    public static NickName NAME = new NickName("voip");

    public CableVoiceService(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE,LEVEL,NAME,null);
        model.getServiceLevelUnit().add(this);
    }

    //children

    private DialToneAccess dialToneAccess;
    private List<SwitchFeature> switchFeatureList = new ArrayList<SwitchFeature>();
    private VoiceMail voiceMail;

    public DialToneAccess getDialToneAccess() {
        return dialToneAccess;
    }

    public void setDialToneAccess(DialToneAccess dialToneAccess) {
        this.dialToneAccess = dialToneAccess;
    }

    public List<SwitchFeature> getSwitchFeatureList() {
        return switchFeatureList;
    }

    public VoiceMail getVoiceMail() {
        return voiceMail;
    }

    public void setVoiceMail(VoiceMail voiceMail) {
        this.voiceMail = voiceMail;
    }

    public PhoneNumber getPhoneNumber() {
        if(dialToneAccess==null){
            return null;
        } else {
            return dialToneAccess.getPhoneNumber();
        }
    }
    public ModemId getModemId() {
        return ModemId.extract(getExternalKey());
    }
}
