package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.ModemId;
import dk.yousee.smp5.casemodel.vo.cablebb.CableBBService;
import dk.yousee.smp5.casemodel.vo.cablebb.InetAccess;
import dk.yousee.smp5.casemodel.vo.emta.AddnCpe;
import dk.yousee.smp5.casemodel.vo.emta.DeviceControl;
import dk.yousee.smp5.casemodel.vo.emta.HsdAccess;
import dk.yousee.smp5.casemodel.vo.emta.StdCpe;
import dk.yousee.smp5.casemodel.vo.emta.VoipAccess;
import dk.yousee.smp5.casemodel.vo.voiceline.DialToneAccess;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.OrderService;
import dk.yousee.smp5.order.model.ProvisionStateEnum;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Oct 13, 2010 Time: 3:16:21 PM
 * The usecase used for change the existing customer's MacAddress.
 * https://yousee.jira.com/wiki/display/SMP/Environment+%28systems+overview%29
 * https://yousee.jira.com/wiki/display/KASIA/provisioning-development
 * https://yousee.jira.com/wiki/pages/viewpageattachments.action?pageId=12779807
 * mvn -f bss-adapter-war/pom.xml jetty:run -Dhttp.proxyHost=sltarray02.tdk.dk
 * -Dhttp.proxyPort=8080 mvndebug -f bss-adapter-war/pom.xml jetty:run
 * -Dhttp.proxyHost=sltarray02.tdk.dk -Dhttp.proxyPort=8080 (To debug) mvndebug
 * -f bss-adapter-war/pom.xml -Djetty.port=9999 jetty:run
 * -Dhttp.proxyHost=sltarray02.tdk.dk -Dhttp.proxyPort=8080 (To debug)
 * <activation> <activeByDefault>true</activeByDefault> </activation>
 * <p/>
 * mvn jetty:run -Djetty.port=9999 -Dhttp.proxyHost=sltarray02.tdk.dk
 * -Dhttp.proxyPort=8080
 */
public class MacAddressCase extends AbstractCase {
	/**
	 * Constructor with the customer account.
	 *
	 * @param acct
	 *            account id
	 * @param service
	 *            service to sigma
	 */
	public MacAddressCase(Acct acct, OrderService service) {
		super(acct, service);
	}

	public MacAddressCase(SubscriberModel model, OrderService service) {
		super(model, service);
	}

	public CableBBService findServiceByActivationCode(String modemActivationCode) {
		return getModel().find().firstCableBBService(modemActivationCode);
	}

	/**
	 * change the customer's Mac Address for HsdAccess, externalKey is the key
	 * of service level plan.
	 *
	 * @param macAddress
	 *            mac address to assign
	 * @param hsdAccessData
	 *            in
	 * @param modemId
	 *            modem used
	 * @return model instance
	 * @throws BusinessException
	 */
	public HsdAccess assignCMMacAddressForHsdAccess(String macAddress, HsdAccessData hsdAccessData, ModemId modemId, String mtaMac) throws BusinessException {
		HsdAccess ha = getModel().alloc().HsdAccess(modemId);
		if (ha.getServicePlanState() == ProvisionStateEnum.COURTESY_BLOCK) {
			ha.sendAction(Action.SUSPEND);
		}
		ha.data_port_id.setValue(macAddress);
		if (hsdAccessData != null) {
			ha.setCmOwnership(modemId); // "1024102022" ** unique
			ha.cm_technology.setValue(hsdAccessData.getCm_technology());
			ha.equipment_type.setValue(hsdAccessData.getEquipment_type()); // "emta"
			ha.gi_address.setValue(hsdAccessData.getGi_address());
			ha.wifi_capable.setValue(hsdAccessData.getWifi_capable()); // "N"
			ha.class_of_service.setValue(hsdAccessData.getClassOfService());
			ha.max_num_cpe.setValue(hsdAccessData.getMax_num_cpe()); // "5"
			ha.docsis_3_capable.setValue(hsdAccessData.getDocsis_3_capable()); // "N"

			DeviceControl deviceControl = getModel().alloc().DeviceControl(modemId);
			deviceControl.cm_mac.setValue(hsdAccessData.getCm_mac());
			deviceControl.mta_mac.setValue(mtaMac);
			deviceControl.serial_number.setValue(hsdAccessData.getCm_serial_number());
			deviceControl.gi_address.setValue(hsdAccessData.getGi_address());
			deviceControl.sik.setValue(modemId.getId());
			deviceControl.manufacturer.setValue(hsdAccessData.getCm_manufacturer());
			deviceControl.model.setValue(hsdAccessData.getCm_model());
		}

		return ha;
	}

	/**
	 * change the customer's Mac Address for InetAccess, externalKey is the key
	 * of service level plan.
	 *
	 * @param macAddress
	 *            mac address to assign
	 * @param modemId
	 *            modem used
	 * @return model instance
	 */
	public InetAccess assignCMMacAddressForInetAccess(String macAddress, ModemId modemId) {
		InetAccess inetAccess = getModel().alloc().InetAccess(modemId);
		inetAccess.cm_mac.setValue(macAddress);
		return inetAccess;
	}

	/**
	 * change the customer's Mac Address for StdCpe, externalKey is the key of
	 * service level plan.
	 *
	 * @param macAddress
	 *            mac address to assign
	 * @param modemId
	 *            modem used
	 * @return model instance
	 */
	public StdCpe assignCPEMacAddressForStdCpe(String macAddress, ModemId modemId) {
		StdCpe stdCpe = getModel().alloc().StdCpe(modemId);
		if (stdCpe.getServicePlanState() == ProvisionStateEnum.COURTESY_BLOCK) {
			stdCpe.sendAction(Action.SUSPEND);
		}
		stdCpe.cpe_mac.setValue(macAddress);
		return stdCpe;
	}

	/**
	 * change the customer's Mac Address for StdCpe, externalKey is the key of
	 * service level plan.
	 *
	 * @param macAddress
	 *            mac address to assign
	 * @param modemId
	 *            modem used
	 * @return model instance
	 */
	public StdCpe assignCMMacAddressForStdCpe(String macAddress, ModemId modemId) {
		StdCpe stdCpe = getModel().alloc().StdCpe(modemId);
		if (stdCpe.getServicePlanState() == ProvisionStateEnum.COURTESY_BLOCK) {
			stdCpe.sendAction(Action.SUSPEND);
		}
		stdCpe.cm_mac.setValue(macAddress);
		return stdCpe;
	}

	/**
	 * change the customer's Mac Address for VoipAccess, externalKey is the key
	 * of service level plan.
	 *
	 * @param macAddress
	 *            mac address to assign
	 * @param modemId
	 *            modem used
	 * @return model instance
	 */
	public VoipAccess assignMTAMacAddressForVoipAccess(String macAddress, ModemId modemId) {
		VoipAccess voipAccess = getModel().alloc().VoipAccess(modemId);
		voipAccess.mta_mac.setValue(macAddress);
		DialToneAccess dialToneAccess = getModel().find().DialToneAccess(modemId);
		if (dialToneAccess != null && dialToneAccess.dt_has_access.get() == null) {
			dialToneAccess.dt_has_access.add(voipAccess);
		}
		return voipAccess;
	}

	/**
	 * add association for InetAccess
	 *
	 * @param hsdAccess
	 *            service
	 * @param modemId
	 *            modem used
	 * @return model instance
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when some problem with link.
	 */
	public InetAccess addAssocInternet_access_has_emta_cmForInetAccess(HsdAccess hsdAccess, ModemId modemId) throws BusinessException {
		InetAccess inetAccess = getModel().alloc().InetAccess(modemId);
		if (inetAccess.internet_access_has_emta_cm.get() == null) {
			inetAccess.internet_access_has_emta_cm.add(hsdAccess);
		}
		return inetAccess;
	}

	/**
	 * update mac for AddnCpe
	 *
	 * @param modemId
	 *            modem used
	 * @param child_id
	 *            , externalkey for AddnCpe
	 * @param cm_mac
	 *            , cm_mac for AddnCpe
	 * @return model instance
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when relation not exists
	 */
	public AddnCpe updateCm_macForAddnCpe(ModemId modemId, String child_id, String cm_mac) throws BusinessException {
		AddnCpe addnCpe = getModel().find().AddnCpe(modemId, child_id);
		addnCpe.cm_mac.setValue(cm_mac);
		return addnCpe;
	}

	/**
	 * update mac for AddnCpe
	 *
	 * @param modemId
	 *            modem used
	 * @param child_id
	 *            , externalkey for AddnCpe
	 * @param cpe_mac
	 *            , cpe_mac for AddnCpe
	 * @return model instance
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when relation not exists
	 */
	public AddnCpe updateCpe_macForAddnCpe(ModemId modemId, String child_id, String cpe_mac) throws BusinessException {
		AddnCpe addnCpe = null;
		if (child_id == null || child_id.length() == 0) {
			addnCpe = getModel().find().AddnCpe(modemId);
		} else {
			addnCpe = getModel().find().AddnCpe(modemId, child_id);
		}
		if (addnCpe != null) {
			addnCpe.cpe_mac.setValue(cpe_mac);
		}
		return addnCpe;
	}

	/**
	 * add childservice 'AddnCpe'
	 *
	 * @param modemId
	 *            modem used
	 * @param cpe_mac
	 *            , cpe_mac for AddnCpe
	 * @param product_code
	 *            , product_code for AddnCpe
	 * @param cm_mac
	 *            , cm_mac for AddnCpe
	 * @return model instance
	 */
	public AddnCpe addAddnCpe(ModemId modemId, String cpe_mac, String product_code, String cm_mac) {
		AddnCpe addnCpe = getModel().add().AddnCpe(modemId);
		addnCpe.cpe_mac.setValue(cpe_mac);
		addnCpe.cpe_product_code.setValue(product_code);
		addnCpe.cpe_service_id.setValue(addnCpe.getExternalKey());
		addnCpe.cm_mac.setValue(cm_mac);
		return addnCpe;
	}

	/**
	 * delete childservice 'AddnCpe'
	 *
	 * @param modemId
	 *            modem used
	 * @param child_id
	 *            in
	 * @return model instance
	 */
	public AddnCpe deleteAddnCpe(ModemId modemId, String child_id) {
		AddnCpe addnCpe = getModel().find().AddnCpe(modemId, child_id);
		addnCpe.getDefaultOrderData().getParams().clear();
		addnCpe.delete();
		return addnCpe;
	}

	/**
	 * delete childservice 'VoipAccess'/'MTA'
	 *
	 * @param modemId
	 *            modem used
	 * @return model instance, if return null, that means there is no
	 *         voipAccess.
	 */
	public VoipAccess deleteVoipAccess(ModemId modemId) {
		VoipAccess voipAccess = getModel().find().VoipAccess(modemId);
		if (voipAccess != null) {
			voipAccess.getDefaultOrderData().getParams().clear();
			voipAccess.delete();
		}
		return voipAccess;
	}

	/**
	 * add childservice 'VoipAccess'
	 *
	 * @param modemId
	 *            modem used
	 * @param mta_id
	 *            in
	 * @param mta_mac
	 *            in
	 * @return model instance
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when association cannot be established (internal error in
	 *             model ... should be refactored)
	 */
	public VoipAccess addVoipAccess(ModemId modemId, String mta_id, String mta_mac) {
		VoipAccess voipAccess = getModel().add().VoipAccess(modemId);
		voipAccess.mta_id.setValue(mta_id); // "12345678903" ** unique
		voipAccess.mta_mac.setValue(mta_mac); // "33885aa32503" ** unique
		voipAccess.mta_dhcp_rules.setValue("captive");// TODO fix but to remove
														// in prd
		voipAccess.mta_max_port_num.setValue("1");
		voipAccess.port_number.setValue("1");
		DialToneAccess dialToneAccess = getModel().find().DialToneAccess(modemId);
		if (dialToneAccess != null && dialToneAccess.dt_has_access.get() == null) {
			dialToneAccess.dt_has_access.add(voipAccess);
		}
		return voipAccess;
	}

	/**
	 * add childservice 'StdCpe' THIS IS A SAMPLE !!!!
	 *
	 * @param modemId
	 *            modem used
	 * @param hsdAccessData
	 *            the params data for creating cable modem
	 * @return model instance
	 * @throws BusinessException
	 */
	public HsdAccess addHsdAccess(ModemId modemId, HsdAccessData hsdAccessData) throws BusinessException {
		HsdAccess ha = getModel().add().HsdAccess(modemId);
		ha.data_port_id.setValue(hsdAccessData.getCm_mac());
		ha.setCmOwnership(modemId); // "1024102022" ** unique
		ha.cm_technology.setValue(hsdAccessData.getCm_technology());
		ha.equipment_type.setValue(hsdAccessData.getEquipment_type()); // "emta"
		ha.gi_address.setValue(hsdAccessData.getGi_address());
		ha.wifi_capable.setValue(hsdAccessData.getWifi_capable()); // "N"
		ha.class_of_service.setValue(hsdAccessData.getClassOfService());
		ha.max_num_cpe.setValue(hsdAccessData.getMax_num_cpe()); // "5"
		ha.docsis_3_capable.setValue(hsdAccessData.getDocsis_3_capable()); // "N"
		return ha;
	}

	public DeviceControl addDeviceControl(ModemId modemId, HsdAccessData hsdAccessData, String mtaMac) throws BusinessException {
		DeviceControl deviceControl = getModel().add().DeviceControl(modemId);
		deviceControl.cm_mac.setValue(hsdAccessData.getCm_mac());
		deviceControl.mta_mac.setValue(mtaMac);
		deviceControl.serial_number.setValue(hsdAccessData.getCm_serial_number());
		deviceControl.gi_address.setValue(hsdAccessData.getGi_address());
		deviceControl.sik.setValue(modemId.getId());
		deviceControl.manufacturer.setValue(hsdAccessData.getCm_manufacturer());
		deviceControl.model.setValue(hsdAccessData.getCm_model());
		return deviceControl;
	}
	
	public DeviceControl updateDeviceControl(ModemId modemId, HsdAccessData hsdAccessData, String mtaMac) throws BusinessException {
		DeviceControl deviceControl = getModel().add().DeviceControl(modemId);
		deviceControl.cm_mac.setValue(hsdAccessData.getCm_mac());
		deviceControl.mta_mac.setValue(mtaMac);
		deviceControl.serial_number.setValue(hsdAccessData.getCm_serial_number());
		deviceControl.gi_address.setValue(hsdAccessData.getGi_address());
		deviceControl.sik.setValue(modemId.getId());
		deviceControl.manufacturer.setValue(hsdAccessData.getCm_manufacturer());
		deviceControl.model.setValue(hsdAccessData.getCm_model());
		return deviceControl;
	}

	public static class HsdAccessData {
		private String cm_mac;
		private String wifi_capable;
		private String docsis_3_capable;
		private String gi_address;
		private String equipment_type;
		private String svc_provider_nm;
		private String cm_technology;
		private String max_num_cpe;
		private String cm_manufacturer;
		private String cm_serial_number;
		private String cm_model;
		private String classOfService;

		public String getCm_mac() {
			return cm_mac;
		}

		public void setCm_mac(String cm_mac) {
			this.cm_mac = cm_mac;
		}

		public String getWifi_capable() {
			return wifi_capable;
		}

		public void setWifi_capable(String wifi_capable) {
			this.wifi_capable = wifi_capable;
		}

		public String getDocsis_3_capable() {
			return docsis_3_capable;
		}

		public void setDocsis_3_capable(String docsis_3_capable) {
			this.docsis_3_capable = docsis_3_capable;
		}

		public String getGi_address() {
			return gi_address;
		}

		public void setGi_address(String gi_address) {
			this.gi_address = gi_address;
		}

		public String getEquipment_type() {
			return equipment_type;
		}

		public void setEquipment_type(String equipment_type) {
			this.equipment_type = equipment_type;
		}

		public String getSvc_provider_nm() {
			return svc_provider_nm;
		}

		public void setSvc_provider_nm(String svc_provider_nm) {
			this.svc_provider_nm = svc_provider_nm;
		}

		public String getCm_technology() {
			return cm_technology;
		}

		public void setCm_technology(String cm_technology) {
			this.cm_technology = cm_technology;
		}

		public String getMax_num_cpe() {
			return max_num_cpe;
		}

		public void setMax_num_cpe(String max_num_cpe) {
			this.max_num_cpe = max_num_cpe;
		}

		public String getCm_manufacturer() {
			return cm_manufacturer;
		}

		public void setCm_manufacturer(String cm_manufacturer) {
			this.cm_manufacturer = cm_manufacturer;
		}

		public String getCm_model() {
			return cm_model;
		}

		public void setCm_model(String cm_model) {
			this.cm_model = cm_model;
		}

		public String getCm_serial_number() {
			return cm_serial_number;
		}

		public void setCm_serial_number(String cm_serial_number) {
			this.cm_serial_number = cm_serial_number;
		}

		public String getClassOfService() {
			return classOfService;
		}

		public void setClassOfService(String classOfService) {
			this.classOfService = classOfService;
		}

	}

	/**
	 * Update the customer's WiFi settings such as gw.channel.id, psk, ss_id -
	 * See PCR13. Copied from CableBBCase (JLo 2011-10-03).
	 * <p>
	 * 
	 * @param modemId
	 *            modem used
	 * @param gw_ch_id
	 *            gw_ch_id, null means unchanged value and $generate will
	 *            autogenerate a new value
	 * @param ss_id
	 *            ss_id, null means unchanged value and $generate will
	 *            autogenerate a new value
	 * @param psk
	 *            psk , null means unchanged value and $generate will
	 *            autogenerate a new value
	 * @return model instance or null if customer does not have wifi service
	 */
	public InetAccess updateSMPWiFi(ModemId modemId, String gw_ch_id, String psk, String ss_id, String gw_ch_5g) {
		InetAccess inetAccess = getModel().find().InetAccess(modemId);
		if (inetAccess != null && inetAccess.wifi_security_disabled.getValue().equals("false")) {
			if (gw_ch_id != null) {
				inetAccess.gw_channel_id.setValue(gw_ch_id);
			}
			if (psk != null) {
				inetAccess.psk.setValue(psk);
			}
			if (ss_id != null) {
				inetAccess.ss_id.setValue(ss_id);
			}
			if (gw_ch_5g != null) {
				inetAccess.gw_channel_id_5g.setValue(gw_ch_5g);
			}
		}
		return inetAccess;
	}

	// 17490
	public InetAccess updateSMPWiFi(ModemId modemId, String gw_ch_id, String psk, String ss_id, String gw_ch_5g, String Psk_5g, String Ss_id_5g) {
		InetAccess inetAccess = getModel().find().InetAccess(modemId);
		if (inetAccess != null && inetAccess.wifi_security_disabled.getValue().equals("false")) {
			if (gw_ch_id != null) {
				inetAccess.gw_channel_id.setValue(gw_ch_id);
			}
			if (psk != null) {
				inetAccess.psk.setValue(psk);
			} else {
				inetAccess.psk.setValue(Psk_5g);
			}
			if (ss_id != null) {
				inetAccess.ss_id.setValue(ss_id);
			}
			if (gw_ch_5g != null) {
				inetAccess.gw_channel_id_5g.setValue(gw_ch_5g);
			}
			if (Psk_5g != null) {
				inetAccess.psk_5g.setValue(Psk_5g);
			}
			if (Ss_id_5g != null) {
				inetAccess.ss_id_5g.setValue(Ss_id_5g);
			}

		}
		return inetAccess;
	}
}
