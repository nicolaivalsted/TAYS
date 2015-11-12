package dk.yousee.smp5.casemodel.vo.ott;

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
 *         Date: 21/10/2015 Time: 11:07:24
 */
public class OTTService extends BasicUnit {
	private static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "ott_services_composed");
	private static NickName NAME = new NickName("ott");

	private List<OTTSubscription> ottSubscriptions = new ArrayList<OTTSubscription>();

	public OTTService(SubscriberModel model, String externalKey) {
		super(model, externalKey, TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	public List<OTTSubscription> getOttSubscriptions() {
		return ottSubscriptions;
	}

	public void setOttSubscription(List<OTTSubscription> ottSubscriptions) {
		this.ottSubscriptions = ottSubscriptions;
	}

}