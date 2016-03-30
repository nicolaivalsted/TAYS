package dk.yousee.smp5.casemodel.vo.sikpakke;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 30/03/2016 Time: 12:33:53
 */
public class SikkerhedspakkeService extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "security_package_composed");
	public static NickName NAME = new NickName("security_package");

	public SikkerhedspakkeService(SubscriberModel model, String externalKey) {
		super(model, externalKey, TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	private Sikkerhedspakke sikkerhedspakke;

	public Sikkerhedspakke getSikkerhedspakke() {
		return sikkerhedspakke;
	}

	public void setSikkerhedspakke(Sikkerhedspakke sikkerhedspakke) {
		this.sikkerhedspakke = sikkerhedspakke;
	}

	public String getSik() {
		return getSikkerhedspakke() == null ? null : getSikkerhedspakke().sik.getValue();
	}

}
