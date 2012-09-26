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
 * Time: 1:37:14 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.5.3	Additional CPE 
 */
public class AddnCpe  extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType( ServicePrefix.SubSvcSpec,"additional_cpe");

    public AddnCpe(SubscriberModel model, String externalKey, CableBBService parent) {
        super(model, externalKey, TYPE, LEVEL, parent);
        parent.getAddnCpe().add(this);
        cpe_service_id.updateValue(externalKey);
    }

    //Type.FEATURE
    public PropHolder cpe_service_id = new PropHolder(this, "cpe_service_id", true);
    public PropHolder cpe_mac = new PropHolder(this, "cpe_mac", true);
    public PropHolder cpe_product_code = new PropHolder(this, "cpe_product_code", true);
    public PropHolder cm_mac = new PropHolder(this, "cm_mac", true);
//    public PropHolder reason_code = new PropHolder(this, "reason_code");
    public PropHolder suspend_billing = new PropHolder(this, "suspend_billing");
    public PropHolder suspend_abuse = new PropHolder(this, "suspend_abuse");

}
