package dk.yousee.smp5.casemodel.vo.voiceline;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.PhoneNumber;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

public class VoiceMail extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "primary_voicemail_box");

	public VoiceMail(SubscriberModel model, String externalKey, VoiceService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setVoiceMail(this);
	}

	public PropHolder randy_status = new PropHolder(this, "randy_status");
	private PropHolder telephone_number = new PropHolder(this, "telephone_number", true);

	public PhoneNumber getPhoneNumber() {
		return PhoneNumber.create(telephone_number.getValue());
	}

	public void setPhoneNumber(PhoneNumber phoneNumber) {
		if (phoneNumber == null)
			throw new IllegalArgumentException("Phone number can never be null for VoiceMail");
		telephone_number.setValue(phoneNumber.getPhoneNumber());
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(" telephone_number=").append(getPhoneNumber());
		sb.append('}');
		return sb.toString();
	}
}
