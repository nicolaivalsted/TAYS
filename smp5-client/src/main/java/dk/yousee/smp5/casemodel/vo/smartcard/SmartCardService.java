package dk.yousee.smp5.casemodel.vo.smartcard;

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
 *         Date: 18/02/2016 Time: 12:08:44
 */
public class SmartCardService extends BasicUnit {
	private static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "smartcard_services_composed");
	private static NickName NAME = new NickName("smartcard");

	private List<SmartCard> smartCards = new ArrayList<SmartCard>();

	/**
	 * @param model
	 * @param externalKey
	 * @param type
	 * @param level
	 * @param name
	 * @param parent
	 */
	public SmartCardService(SubscriberModel model, String externalKey) {
		super(model, externalKey, TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	public List<SmartCard> getSmartCards() {
		return smartCards;
	}

	public void setSmartCards(List<SmartCard> smartCards) {
		this.smartCards = smartCards;
	}

}
