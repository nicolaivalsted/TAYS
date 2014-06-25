package dk.yousee.smp.casemodel.vo.mofibo;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 *
 * @author simonk
 */
public class Mofibo extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "mofibo");

    public Mofibo(SubscriberModel model, String externalKey, MofiboService parent) {
        super(model, externalKey, TYPE, LEVEL, null, parent);
        parent.setMofibo(this);
    }

    public Mofibo(SubscriberModel model, BusinessPosition position) {
        super(model, model.key().generateUUID(), TYPE, LEVEL, null, new MofiboService(model, model.key().generateUUID()));
        business_position.setValue(position.getId());
        getParent().setMofibo(this);
    }

    public MofiboService getParent() {
        return (MofiboService) super.getParent();
    }

    /**
     * Name of field
     */
    public static final String BUSINESS_POSITION = "business_position";
    /**
     * identifier that identify the subscribers modem among all the modems the
     * subscriber has. Field value can be "1", "2" etc. It is only required to
     * be unique for the subscriber. So two different subscribers can both have
     * position called "1" The objective is to manage relation to CRM
     * subscription. This is an instance key to service plan. It is normally
     * never modified. YouSee will fill in modem_id / aftale Nr / ... tbd.
     */
    protected PropHolder business_position = new PropHolder(this, BUSINESS_POSITION);

    BusinessPosition getPosition() {
        return BusinessPosition.create(business_position.getValue());
    }

    public static final String MOFIBO_PCODE = "mofibo_pcode";
    public static final String MOFIBO_PRODUCT = "1302007";
    public PropHolder mofibo_pcode = new PropHolder(this, MOFIBO_PCODE, true);
}
