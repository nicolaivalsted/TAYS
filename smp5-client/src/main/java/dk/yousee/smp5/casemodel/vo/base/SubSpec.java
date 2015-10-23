/**
 * 
 */
package dk.yousee.smp5.casemodel.vo.base;

import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;


/**
 * @author m64746
 *
 * Date: 14/10/2015
 * Time: 13:42:25
 * Used only for deleteSubscription in SubscriberCase.
 */
public class SubSpec extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.SUBSPEC;
	public static OrderDataType TYPE = SubContactSpec.TYPE;

    public SubSpec(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE, LEVEL,null ,null);
        model.getServiceLevelUnit().add(this);
    }

}
