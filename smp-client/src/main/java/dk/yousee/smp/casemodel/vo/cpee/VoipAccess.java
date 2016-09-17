package dk.yousee.smp.casemodel.vo.cpee;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 12, 2010
 * Time: 2:50:47 PM
 * Value object for eMTA Voip
 * Data structure reference to YouSee Data Migration Requirements: 5.3.2 PacketCable VoIP Access
 */
public class VoipAccess extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"smp_emta_pc_voip_access");


    public VoipAccess(SubscriberModel model, String externalKey, CpeComposedService parent) {
        super(model, externalKey, TYPE, LEVEL,null, parent);
        parent.setVoipAccess(this);
        mta_service_id.updateValue(externalKey);
    }

    //Type.FEATURE
    public PropHolder mta_service_id = new PropHolder(this, "mta_service_id", true);
    public PropHolder mta_mac = new PropHolder(this, "mta_mac", true);
    public PropHolder mta_id = new PropHolder(this, "mta_id", true);

    public PropHolder mta_fqdn = new PropHolder(this, "mta_fqdn", false);
    public PropHolder cm_mac = new PropHolder(this, "cm_mac", false);
    public PropHolder cmts = new PropHolder(this, "cmts", false);
    public PropHolder mta_max_port_num = new PropHolder(this, "mta_max_port_num");

}
