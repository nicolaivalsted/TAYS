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
 * Time: 2:11:24 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.5.5	SMP Email Server Unblock
 */
public class SMPEmail extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"email_server_unblock");

    public SMPEmail(SubscriberModel model, String externalKey, CableBBService parent) {
        super(model, externalKey, TYPE, LEVEL,null, parent);
        parent.setSmpEmail(this);
        email_server_unblock_service_id.updateValue(externalKey);
    }

    //Type.FEATURE
    public PropHolder email_server_unblock_service_id = new PropHolder(this, "email_server_unblock_service_id", true);
    public PropHolder email_server_unblock_product_code = new PropHolder(this, "email_server_unblock_product_code", true);
}
