package dk.yousee.smp.casemodel.vo.cbp;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 21, 2010
 * Time: 1:50:59 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.5.6	SMP WiFi Service
 */
public class BSA extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"bit_stream_access");

    public BSA(SubscriberModel model, String externalKey, CableBBService parent) {
        super(model, externalKey, TYPE, LEVEL,null, parent);
        parent.setBsa(this);
    }

    //Type.FEATURE
    public PropHolder vrf = new PropHolder(this, "vrf", true);
    public PropHolder isp_name = new PropHolder(this, "isp_name", false);
    
}
