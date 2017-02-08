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
	public PropHolder sid = new PropHolder(this, "sid", false);
	public PropHolder cnr = new PropHolder(this, "cnr", false);
	public PropHolder la = new PropHolder(this, "la", false);
	public PropHolder identifier = new PropHolder(this, "identifier", false);
	public PropHolder type = new PropHolder(this, "type", false);
	public PropHolder branding = new PropHolder(this, "branding", false);
	public PropHolder email = new PropHolder(this, "email", false);
	public PropHolder gllid = new PropHolder(this, "gllid", false);
	public PropHolder id = new PropHolder(this, "id", false);
	public PropHolder lid = new PropHolder(this, "lid", false);
	public PropHolder plan = new PropHolder(this, "plan", false);
	public PropHolder source_code = new PropHolder(this, "source_code", false);

	public FordelSubscription(SubscriberModel model, String externalKey, FordelComposed parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.getFordelSubscriptions().add(this);
	}

	public FordelComposed getParent() {
		return (FordelComposed) super.getParent();
	}

}
