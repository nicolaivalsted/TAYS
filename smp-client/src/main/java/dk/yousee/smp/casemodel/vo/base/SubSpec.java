package dk.yousee.smp.casemodel.vo.base;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.Constants;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 29, 2010
 * Time: 1:51:02 PM
 * Used only for deleteSubscription in SubscriberCase.
 */
public class SubSpec  extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.SUBSPEC;

    //todo this value of TYPE is identical to SubContactSpec ??????????????????? it must be some kind of bug !!!!!
    public static OrderDataType TYPE = Constants.SERVICE_TYPE_CONTACT_SPEC;

    public SubSpec(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE, LEVEL, null);
        model.getServiceLevelUnit().add(this);
    }

}

