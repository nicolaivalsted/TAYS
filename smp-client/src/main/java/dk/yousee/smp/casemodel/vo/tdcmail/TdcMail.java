package dk.yousee.smp.casemodel.vo.tdcmail;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 *
 * @author m27236
 */
public class TdcMail extends BasicUnit{
    
    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"tdcmail_service");
    
    public TdcMail(SubscriberModel model, String externalKey, TdcMailService parent) {
        super(model, externalKey, TYPE, LEVEL,null, parent);
        parent.setTdcMail(this);
    }
    
    public TdcMail(SubscriberModel model, BusinessPosition position) {
        super(model, model.key().generateUUID(), TYPE, LEVEL, null, new TdcMailService(model, model.key().generateUUID()));
        business_position.setValue(position.getId());
        getParent().setTdcMail(this);
    }
    
    @Override
    public TdcMailService getParent(){
        return (TdcMailService)super.getParent();
    }
    
    public static final String BUSINESS_POSITION = "business_position";
    protected PropHolder business_position = new PropHolder(this, BUSINESS_POSITION);
    
    BusinessPosition getPosition() {
        return BusinessPosition.create(business_position.getValue());
    }
    
    public static final String YSPRO_PCODE = "yspro_pcode";
    public PropHolder yspro_pcode = new PropHolder(this, YSPRO_PCODE, true);
    
}
