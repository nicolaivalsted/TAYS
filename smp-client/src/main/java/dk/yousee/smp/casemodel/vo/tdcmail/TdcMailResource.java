package dk.yousee.smp.casemodel.vo.tdcmail;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 *
 * @author m27236
 */
public class TdcMailResource extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "tdcmail_resource");

    public TdcMailResource(SubscriberModel model, String externalKey, TdcMailService parent) {
        super(model, externalKey, TYPE, LEVEL, null, parent);
        parent.setTdcMailResource(this);
    }
    public static final String YSPRO_PROVISIONING_ID = "yspro_provisioningid";
    public PropHolder yspro_provisioning_id = new PropHolder(this, YSPRO_PROVISIONING_ID);
    public static final String KPM_NUMBER = "kpm_number";
    public PropHolder kpm_number = new PropHolder(this, KPM_NUMBER, true);
}
