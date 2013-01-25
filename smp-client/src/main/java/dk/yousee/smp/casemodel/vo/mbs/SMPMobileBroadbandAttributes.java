package dk.yousee.smp.casemodel.vo.mbs;

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
 * Time: 2:38:27 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.7.2	SMP Mobile Broadband Service Attributes 
 */
public class SMPMobileBroadbandAttributes extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"mobilebb_service_attribs");

    
    public SMPMobileBroadbandAttributes(SubscriberModel model, String externalKey, MobileBBService parent) {
        super(model, externalKey, TYPE, LEVEL, parent);
        parent.getSmpMobileBroadbandAttributes().add(this);
        mobilebb_service_attribs_id.updateValue(externalKey);
    }

    //Type.FEATURE
    public PropHolder mobilebb_service_attribs_id = new PropHolder(this, "mobilebb_service_attribs_id", true);
    public PropHolder service_type = new PropHolder(this, "service_type"); 
}
