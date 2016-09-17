package dk.yousee.smp5.casemodel.vo.tdcmail;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * Service class for TDC Mail
 * 
 * @author m27236
 */
public class TdcMailService extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "tdcmail_composed");
	public static NickName NAME = new NickName("tdcmail");

	private TdcMail tdcMail;

	public TdcMailService(SubscriberModel model, String externalKey) {
		super(model, externalKey, TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	public TdcMail getTdcMail() {
		return tdcMail;
	}

	public void setTdcMail(TdcMail tdcMail) {
		this.tdcMail = tdcMail;
	}

	public String getSik() {
		return getTdcMail() == null ? null : getTdcMail().sik.getValue();
	}

	public void setSik(String sik) {
		getTdcMail().sik.setValue(sik);
	}
}
