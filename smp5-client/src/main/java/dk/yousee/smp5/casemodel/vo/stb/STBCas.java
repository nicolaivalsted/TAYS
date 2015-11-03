package dk.yousee.smp5.casemodel.vo.stb;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.Constants;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 27/10/2015 Time: 14:20:41
 */
public class STBCas extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "cpe_cas");

	public PropHolder business_position = new PropHolder(this, Constants.BUSINESS_POSITION, true);
	public PropHolder macAddress = new PropHolder(this, "macAddress", true);
	public PropHolder serialNumber = new PropHolder(this, "serialNumber", true);
	public PropHolder acct = new PropHolder(this, "acct", true);
	public PropHolder manufacturer = new PropHolder(this, "manufacturer", true);
	public PropHolder model = new PropHolder(this, "model", true);

	public STBCas(SubscriberModel model, String externalKey, VideoCPE parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setStbCAS(this);
	}

	public VideoCPE getParent() {
		return (VideoCPE) super.getParent();
	}

}
