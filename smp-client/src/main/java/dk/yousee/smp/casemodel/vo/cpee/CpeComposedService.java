package dk.yousee.smp.casemodel.vo.cpee;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;


/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 12, 2010
 * Time: 2:40:40 PM
 * The object to contain the hsdAccess object and voipAccess object.
 * Data structure reference to YouSee Data Migration Requirements: 5.4 CPE Services Definition
 */
public class CpeComposedService extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType( ServicePrefix.SubSvcSpec,"cpe_composed");
    public static NickName NAME = new NickName("modem");

    /**
     * Contains modemId as long as the service is under construction
     */
    private ModemId tempCmOwnership;

    /**
     * Used from parser to create model element
     * @param model in
     * @param externalKey key
     */
    public CpeComposedService(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE,LEVEL,NAME,null);
        model.getServiceLevelUnit().add(this);
    }

    /**
     * Used under construction of new model elements before creation (alloc/add).
     * @param model in
     * @param cmOwnership in
     */
    public CpeComposedService(SubscriberModel model, ModemId cmOwnership) {
        this(model,model.key().CpeComposedService(cmOwnership));
        this.tempCmOwnership =cmOwnership;
    }

    //children

    private HsdAccess hsdAccess;
    private VoipAccess voipAccess;

    /**
     * Get activation key to this modem
     * @return key to the service
     */
    public ModemId getCmOwnership() {
        if(hsdAccess==null){
            return tempCmOwnership;
        } else {
            return hsdAccess.getCmOwnership();
        }
    }

    public HsdAccess getHsdAccess() {
        return hsdAccess;
    }

    public void setHsdAccess(HsdAccess hsdAccess) {
        this.hsdAccess = hsdAccess;
    }

    public VoipAccess getVoipAccess() {
        return voipAccess;
    }

    public void setVoipAccess(VoipAccess voipAccess) {
        this.voipAccess = voipAccess;
    }
}
