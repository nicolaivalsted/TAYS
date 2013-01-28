package dk.yousee.smp.casemodel.vo.base;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.Constants;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 15, 2010
 * Time: 10:59:08 AM
 * Address sub specification, a special Service Plan :-)
 * 
 */
public final class SampSub extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"samp_sub");;
    public static NickName NAME = new NickName("samp-sub");

    public SampSub(SubscriberModel model,String externalKey) {
        super(model, externalKey,  TYPE,LEVEL,NAME,null);
        model.getServiceLevelUnit().add(this);
    }

    //Type.ASSOC
//    public AssociationHolder service_on_address = new AssociationHolder(this, "service_on_address", SubAddressSpec.TYPE );
}
