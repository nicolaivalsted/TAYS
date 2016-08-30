package dk.yousee.smp.casemodel.vo.cvp;

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
 * Time: 2:59:38 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.4.1.2	Switch Feature
 */
public class SwitchFeature extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType( ServicePrefix.SubSvcSpec,"smp_switch_feature_pkg_std");

    public SwitchFeature(SubscriberModel model, String externalKey, CableVoiceService parent) {
        super(model, externalKey, TYPE, LEVEL,null, parent);
        parent.getSwitchFeatureList().add(this);
        switch_feature_service_id.updateValue(externalKey);
    }

    //Type.FEATURE
    public PropHolder switch_feature_service_id = new PropHolder(this, "switch_feature_service_id", true);
}
