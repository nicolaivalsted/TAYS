package dk.yousee.smp5.casemodel.vo.fordel;

import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: Jan 23, 2017 Time: 10:41:25 AM
 */
public class FordelComposed extends BasicUnit {
	private static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "fordel_composed");
	private static NickName NAME = new NickName("fordel");

	private List<FordelSubscription> fordelSubscriptions = new ArrayList<FordelSubscription>();

	public FordelComposed(SubscriberModel model, String externalKey) {
		super(model, externalKey, TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	public List<FordelSubscription> getFordelSubscriptions() {
		return fordelSubscriptions;
	}

	public void setFordelSubscription(List<FordelSubscription> fordelSubscriptions) {
		this.fordelSubscriptions = fordelSubscriptions;
	}

}
