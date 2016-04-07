package dk.yousee.smp5.casemodel.vo.tdcmail;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 *
 * @author m27236
 */
public class TdcMail extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "tdcmail_service");

	public TdcMail(SubscriberModel model, String externalKey, TdcMailService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setTdcMail(this);
	}

	public TdcMail(SubscriberModel model, String id) {
		super(model, model.key().generateUUID(), TYPE, LEVEL, null, new TdcMailService(model, model.key().generateUUID()));
		sik.setValue(id);
		getParent().setTdcMail(this);
	}

	@Override
	public TdcMailService getParent() {
		return (TdcMailService) super.getParent();
	}

	protected PropHolder sik = new PropHolder(this, "sik");
	public PropHolder yspro_pcode = new PropHolder(this, "yspro_pcode", true);
    public PropHolder kpm_number = new PropHolder(this, "kpm_number", true);
    public PropHolder yspro_provisioning_id = new PropHolder(this, "yspro_provisioningid");

}
