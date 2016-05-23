package dk.yousee.smp5.casemodel.vo.voiceline;

import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.ModemId;
import dk.yousee.smp5.casemodel.vo.PhoneNumber;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

public class VoiceService extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "smp_voice_line");
	public static NickName NAME = new NickName("voip");

	public VoiceService(SubscriberModel model, String externalKey) {
		super(model, externalKey, TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	private DialToneAccess dialToneAccess;
	private List<SwitchFeature> switchFeatureList = new ArrayList<SwitchFeature>();
	private VoiceMail voiceMail;

	public DialToneAccess getDialToneAccess() {
		return dialToneAccess;
	}

	public void setDialToneAccess(DialToneAccess dialToneAccess) {
		this.dialToneAccess = dialToneAccess;
	}

	public List<SwitchFeature> getSwitchFeatureList() {
		return switchFeatureList;
	}

	public VoiceMail getVoiceMail() {
		return voiceMail;
	}

	public void setVoiceMail(VoiceMail voiceMail) {
		this.voiceMail = voiceMail;
	}

	public PhoneNumber getPhoneNumber() {
		if (dialToneAccess == null) {
			return null;
		} else {
			return dialToneAccess.getPhoneNumber();
		}
	}

	public ModemId getModemId() {
		if (dialToneAccess == null) {
			return null;
		} else {
			return ModemId.create(dialToneAccess.modem_id.getValue());
		}
	}

	/**
	 * Business Position used to be modemId, therefore this code to be
	 * compatible
	 * 
	 * @return the best fitting business position
	 */
	public BusinessPosition getPosition() {
		BusinessPosition bp;
		ModemId m = getModemId();
		if (getDialToneAccess() == null) {
			if (m == null) {
				bp = null;
			} else {
				bp = BusinessPosition.create(m.getId());
			}
		} else {
			bp = getDialToneAccess().getPosition();
			if (bp == null) {
				if (m == null) {
					bp = null;
				} else {
					bp = BusinessPosition.create(m.getId());
				}
			}
		}
		return bp;
	}

}
