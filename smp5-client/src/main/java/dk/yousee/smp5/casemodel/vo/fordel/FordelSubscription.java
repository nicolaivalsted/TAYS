package dk.yousee.smp5.casemodel.vo.fordel;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: Jan 23, 2017 Time: 10:41:53 AM
 */
public class FordelSubscription extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "fordel_subscription");

	public PropHolder sik = new PropHolder(this, "sik", true);
	public PropHolder sid = new PropHolder(this, "sid", true);
	public PropHolder cnr = new PropHolder(this, "cnr", true);
	public PropHolder la = new PropHolder(this, "la", true);
	public PropHolder identifier = new PropHolder(this, "identifier", true);
	public PropHolder type = new PropHolder(this, "type", true);
	public PropHolder branding = new PropHolder(this, "branding", true);
	public PropHolder email = new PropHolder(this, "email", true);
	public PropHolder gllid = new PropHolder(this, "gllid", true);
	public PropHolder id = new PropHolder(this, "id", true);
	public PropHolder lid = new PropHolder(this, "lid", true);
	public PropHolder plan = new PropHolder(this, "plan", true);
	public PropHolder source_code = new PropHolder(this, "source_code", true);

	public FordelSubscription(SubscriberModel model, String externalKey, FordelComposed parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.getFordelSubscriptions().add(this);
	}

	public FordelComposed getParent() {
		return (FordelComposed) super.getParent();
	}

}
