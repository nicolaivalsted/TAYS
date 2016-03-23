package dk.yousee.smp5.casemodel.vo.mail;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * Definition of mail service
 */
public class Mail extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "foreningsmail");

	public Mail(SubscriberModel model, String externalKey, ForeningsMailService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setMail(this);
	}

	public Mail(SubscriberModel model) {
		super(model, model.key().generateUUID(), TYPE, LEVEL, null, new ForeningsMailService(model, model.key().generateUUID()));
		getParent().setMail(this);
	}

	public ForeningsMailService getParent() {
		return (ForeningsMailService) super.getParent();
	}

	public PropHolder product_code = new PropHolder(this, "product_code", true);
	public PropHolder product_name = new PropHolder(this, "product_name");
	public PropHolder customer_id = new PropHolder(this, "customer_id");
	protected PropHolder conversation = new PropHolder(this, "conversation");
	public PropHolder sik = new PropHolder(this, "sik");

	public Boolean getConversation() {
		Boolean res;
		if (conversation.hasValue()) {
			res = Boolean.parseBoolean(conversation.getValue());
		} else {
			res = null;
		}
		return res;
	}

	public void setConversation(Boolean conversation) {
		if (conversation == null) {
			this.conversation.clearValue();
		} else {
			this.conversation.setValue(conversation.toString());
		}
	}

}
