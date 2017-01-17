package dk.yousee.smp5.casemodel.vo.cablebb;

import java.util.Random;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.ModemId;
import dk.yousee.smp5.casemodel.vo.emta.HsdAccess;
import dk.yousee.smp5.casemodel.vo.helpers.AssociationHolder;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

public class InetAccess extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "internet_access");

	public PropHolder rate_codes = new PropHolder(this, "rate_codes", true);
	public PropHolder svc_provider_nm = new PropHolder(this, "svc_provider_nm");
	public PropHolder upstream_speed = new PropHolder(this, "upstream_speed");
	public PropHolder config_file_override = new PropHolder(this, "config_file_override");
	public PropHolder bill_ack = new PropHolder(this, "bill_ack");
	public PropHolder bottom_up_provisioned = new PropHolder(this, "bottom_up_provisioned");
	public PropHolder num_of_ips = new PropHolder(this, "num_of_ips");
	public PropHolder aup = new PropHolder(this, "aup");
	public PropHolder email_cos = new PropHolder(this, "email_cos");
	public PropHolder cm_mac = new PropHolder(this, "cm_mac");
	public PropHolder product_name = new PropHolder(this, "product_name");
	public PropHolder upstream_bonding_enabled = new PropHolder(this, "upstream_bonding_enabled");
	public PropHolder upstream_channel_bonding = new PropHolder(this, "upstream_channel_bonding");
	public PropHolder downstream = new PropHolder(this, "downstream_bw_profile");
	public PropHolder upstream = new PropHolder(this, "upstream_bw_profile");
	public PropHolder allowed_cpe = new PropHolder(this, "allowed_cpe");
	public PropHolder suspend = new PropHolder(this, "suspend");
	public PropHolder suspend_reason = new PropHolder(this, "suspend_reason");

	public PropHolder modem_id = new PropHolder(this, "modem_id");
	protected PropHolder modem_activation_code = new PropHolder(this, "modem_activation_code");
	public PropHolder sik = new PropHolder(this, "sik");

	public AssociationHolder internet_access_has_emta_cm = new AssociationHolder(this, "internet_has_access", HsdAccess.TYPE);

	// BSA
	public PropHolder vrf = new PropHolder(this, "vrf", true);
	public PropHolder isp_name = new PropHolder(this, "isp_name", false);
	public PropHolder sip_enabled = new PropHolder(this, "sip_enabled", false);

	// email server unblock
	public PropHolder email_server_enable = new PropHolder(this, "email_server_enable", true);

	// wifi
	public PropHolder ss_id = new PropHolder(this, "ss_id", true);
	public PropHolder psk = new PropHolder(this, "psk", true);
	public PropHolder psk_5g = new PropHolder(this, "psk_5g", true);
	public PropHolder gw_channel_id = new PropHolder(this, "gw_channel_id", true);
	public PropHolder ss_id_5g = new PropHolder(this, "ss_id_5g", false); // readonly!
	public PropHolder gw_channel_id_5g = new PropHolder(this, "gw_channel_id_5g", false);
	public PropHolder wifi_security_disabled = new PropHolder(this, "wifi_security_disabled", true);

	// wallplug
	public PropHolder wallplug = new PropHolder(this, "d31_wallplug");

	public PropHolder diplexer = new PropHolder(this, "diplexer");

	public InetAccess(SubscriberModel model, String externalKey, CableBBService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setInetAccess(this);
	}

	/**
	 * Assign modemId to servicePlan
	 *
	 * @param modemId
	 *            in cannot be null
	 */
	public void setModemId(ModemId modemId) {
		if (modemId == null) {
			throw new IllegalArgumentException("ModemId can never be null on InetAccess, it is the primary key to BB-service");
		} else {
			modem_id.setValue(modemId.getId());
		}
	}

	/**
	 * Method to return activation code, will return modemId if property is
	 * missing
	 * <p>
	 * BB's from Casper / Kasia uses modemId as self activation code.<br/>
	 * BB from M5 uses a PIN code like activation code that has nothing to do
	 * with modemId From ASU this tweak is hidden by this method.<
	 * </p>
	 * <p>
	 * This method will return modem_activation_code from property if it exists
	 * Otherwise it will return modemId
	 * </p>
	 * ASU can use this one to validate users / authentication in stead of
	 * assuming it is modemId
	 *
	 * @return modem activation code. (PIN code)
	 */
	public String getModemActivationCode() {
		if (modem_activation_code.hasValue()) {
			return modem_activation_code.getValue();
		} else {
			return modem_id.getValue();
		}
	}

	/**
	 * Assign activation code
	 *
	 * @param modemActivationCode
	 *            to become PIN - null values are ignored (there fore it legal
	 *            to send a null) Null will be the case in the intermediate
	 *            faces before all software uses modemActivationCode to express
	 *            this situation
	 */
	public void setModemActivationCode(String modemActivationCode) {
		if (modemActivationCode != null) {
			modem_activation_code.setValue(modemActivationCode);
		}
	}

	public CableBBService getParent() {
		return (CableBBService) super.getParent();
	}

	public static String generateSsid() {
		char Letter[] = "abdfghjkmnpqrstuvzxy345679".toCharArray();
		String sSSID = "";
		Random generator = new Random();
		for (int i = 0; i < 8; i++) {
			int randomIndex = generator.nextInt(Letter.length);
			sSSID = sSSID + Letter[randomIndex];
		}
		return sSSID;
	}

	public static String generatePsk() {
		char Letter[] = "abdfghjkmnpqrstuvzxy345679".toCharArray();
		String sWPAKEY = "";
		Random generator = new Random();
		for (int i = 0; i < 16; i++) {

			int randomIndex = generator.nextInt(Letter.length);
			if (i > 7) {
				sWPAKEY = sWPAKEY + Letter[randomIndex];
			}
		}
		return sWPAKEY;
	}

}
