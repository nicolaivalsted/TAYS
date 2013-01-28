package dk.yousee.smp.casemodel.vo.mbs;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 25, 2010
 * Time: 1:42:40 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.3.3	SMP SIM Card
 */
public class SMPSIMCard extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"sim_card");
    public static NickName NAME = new NickName("simcard");

    /**
     * Constructs a simcard service "plan"
     * @param model in
     * @param externalKey key generated by
     * key {@link dk.yousee.smp.casemodel.vo.helpers.Key#SMPSIMCard(dk.yousee.smp.casemodel.vo.ModemId)}
     * where modemId is the variation. <br/>
     * I have seen test-cases where a random number is used. But these test-cases should probably be deleted.
     */
    public SMPSIMCard(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE,LEVEL,NAME,null);
        model.getServiceLevelUnit().add(this);
        sim_card_service_id.updateValue(externalKey);
    }

    //Type.FEATURE
    /**
     * this field is always updated with value == (this SMPSIMCard).getExternalKey()<br/>
     * Why is is so ????
     * Erling: It is just an ID, it is not needed from "our" side, it is an internal reference used by the SMP system
     */
    public PropHolder sim_card_service_id = new PropHolder(this, "sim_card_service_id", true);
    public PropHolder sim_card_id = new PropHolder(this, "sim_card_id", true);
}
