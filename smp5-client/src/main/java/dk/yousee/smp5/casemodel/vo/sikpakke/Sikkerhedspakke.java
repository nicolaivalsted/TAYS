package dk.yousee.smp5.casemodel.vo.sikpakke;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 30/03/2016 Time: 12:34:17
 */
public class Sikkerhedspakke extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "security_package");

	public Sikkerhedspakke(SubscriberModel model, String externalKey, SikkerhedspakkeService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setSikkerhedspakke(this);
	}

	public Sikkerhedspakke(SubscriberModel model) {
		super(model, model.key().generateUUID(), TYPE, LEVEL, null, new SikkerhedspakkeService(model, model.key().generateUUID()));
		getParent().setSikkerhedspakke(this);
	}

	public SikkerhedspakkeService getParent() {
		return (SikkerhedspakkeService) super.getParent();
	}

	public String getSik() {
		return sik.getValue();
	}

	public PropHolder sik = new PropHolder(this, "sik", true);
	public PropHolder yspro_provisioningid = new PropHolder(this, "yspro_provisioningid", false);
	public PropHolder license_type = new PropHolder(this, "licenseType", false);

}
