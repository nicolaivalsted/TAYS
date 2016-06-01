package dk.yousee.smp5.casemodel.vo.voiceline;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

public class VoiceMail extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "a_la_carte_vm_composed");
	public static NickName NAME = new NickName("voip");

	public VoiceMail(SubscriberModel model, String externalKey, VoiceService parent) {
		super(model, externalKey, TYPE, LEVEL, NAME, parent);
		model.getServiceLevelUnit().add(this);
	}

	private MailBox mailBox;

	public MailBox getMailBox() {
		return mailBox;
	}

	public void setMailBox(MailBox mailBox) {
		this.mailBox = mailBox;
	}

}
