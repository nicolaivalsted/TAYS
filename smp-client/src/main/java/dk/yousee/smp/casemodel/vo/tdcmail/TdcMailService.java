package dk.yousee.smp.casemodel.vo.tdcmail;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Service class for TDC Mail
 * @author m27236
 */
public class TdcMailService extends BasicUnit{
    
    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"tdcmail_composed");
    public static NickName NAME = new NickName("tdcmail");

    private TdcMail tdcMail;
    
    private TdcMailResource tdcMailResource;
    
    public TdcMailService(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE, LEVEL, NAME, null); 
    }

    public TdcMail getTdcMail() {
        return tdcMail;
    }

    public void setTdcMail(TdcMail tdcMail) {
        this.tdcMail = tdcMail;
    }

    public TdcMailResource getTdcMailResource() {
        return tdcMailResource;
    }

    public void setTdcMailResource(TdcMailResource tdcMailResource) {
        this.tdcMailResource = tdcMailResource;
    }
    
    public BusinessPosition getPosition() {
        return getTdcMail()==null?null: getTdcMail().getPosition();
    }
    
}
