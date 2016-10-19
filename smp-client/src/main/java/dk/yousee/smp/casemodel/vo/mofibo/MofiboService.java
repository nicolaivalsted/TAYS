package dk.yousee.smp.casemodel.vo.mofibo;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 *
 * @author simonk
 */
public class MofiboService extends BasicUnit {
    
    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"mofibo_composed");
    public static NickName NAME = new NickName("mofibo");

    public MofiboService(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE, LEVEL, NAME,null);
        model.getServiceLevelUnit().add(this);
    }


    //children
    private Mofibo mofibo;

    public Mofibo getMofibo() {
        return mofibo;
    }

    public void setMofibo(Mofibo mofibo) {
        this.mofibo = mofibo;
    }


    public BusinessPosition getPosition() {
        return getMofibo()==null?null: getMofibo().getPosition();
    }
    
}
