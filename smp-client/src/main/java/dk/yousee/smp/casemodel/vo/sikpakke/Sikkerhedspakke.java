package dk.yousee.smp.casemodel.vo.sikpakke;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Definition of sikkerhedspakke.
 */
public class Sikkerhedspakke extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "security_package");


    public Sikkerhedspakke(SubscriberModel model, String externalKey, SikkerhedspakkeService parent) {
        super(model, externalKey, TYPE, LEVEL, null, parent);
        parent.setSikkerhedspakke(this);
    }

    public Sikkerhedspakke(SubscriberModel model, BusinessPosition position) {
        super(model, model.key().generateUUID(), TYPE, LEVEL, null, new SikkerhedspakkeService(model, model.key().generateUUID()));
        business_position.setValue(position.getId());
        getParent().setSikkerhedspakke(this);
    }

    public SikkerhedspakkeService getParent() {
        return (SikkerhedspakkeService) super.getParent();
    }

    /**
     * Name of field
     */
    public static final String BUSINESS_POSITION = "business_position";

    protected PropHolder business_position = new PropHolder(this, BUSINESS_POSITION);

    public BusinessPosition getPosition() {
        return BusinessPosition.create(business_position.getValue());
    }

    public static final String YSPRO_PCODE = "yspro_pcode";
    public PropHolder yspro_pcode = new PropHolder(this, YSPRO_PCODE, true);

    public static final String YSPRO_PROVISIONINGID = "yspro_provisioningid";
    public PropHolder yspro_provisioningid = new PropHolder(this, YSPRO_PROVISIONINGID);
}
