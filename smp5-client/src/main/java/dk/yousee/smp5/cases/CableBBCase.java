package dk.yousee.smp5.cases;

import java.util.List;

import org.apache.log4j.Logger;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.ModemId;
import dk.yousee.smp5.casemodel.vo.cablebb.CableBBService;
import dk.yousee.smp5.casemodel.vo.cablebb.InetAccess;
import dk.yousee.smp5.casemodel.vo.cablebb.SMPStaticIP;
import dk.yousee.smp5.casemodel.vo.cablebb.SuspendHelper.SuspendReasonAbuse;
import dk.yousee.smp5.casemodel.vo.cablebb.SuspendHelper.SuspendReasonBilling;
import dk.yousee.smp5.casemodel.vo.cablebb.SuspendStatus;
import dk.yousee.smp5.casemodel.vo.emta.AddnCpe;
import dk.yousee.smp5.casemodel.vo.emta.HsdAccess;
import dk.yousee.smp5.casemodel.vo.emta.MTAService;
import dk.yousee.smp5.casemodel.vo.emta.StdCpe;
import dk.yousee.smp5.casemodel.vo.voiceline.VoiceService;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;
import dk.yousee.smp5.order.model.ProvisionStateEnum;

/**
 * Created by IntelliJ IDEA. User: aka Date: Oct 14, 2010 Time: 5:01:32 PM<br/>
 * Case for cable broad band
 */
public class CableBBCase extends AbstractCase {
	private static final Logger logger = Logger.getLogger(CableBBCase.class);

	public CableBBCase(Acct acct, OrderService service) {
		super(acct, service);
	}

	public CableBBCase(SubscriberModel model, OrderService service) {
		super(model, service);
	}

	/**
	 * Construct this case based on existing Subscriber Case<br/>
	 * This is a kind of chaining of use-cases. <br/>
	 * <p>
	 * First ask for the customer, eventually create him Then work with cable
	 * broad band.
	 * </p>
	 * ?
	 *
	 * @param customerCase
	 *            subscriber case's
	 */
	public CableBBCase(SubscriberCase customerCase) {
		super(new SubscriberModel(customerCase.getModel().getResponse()), customerCase.getService());
	}

	/**
	 * Return the customers first cable broad band modem
	 *
	 * @return the first customer have
	 */
	public ModemId firstModem() {
		List<CableBBService> list = getModel().find().CableBBService();
		if (list.isEmpty()) {
			return null;
		}
		CableBBService first = list.get(0);
		return first.getModemId();

	}

	/**
	 * Use-case 8: Erling: Lav en ordre, som indeholder en opret-service
	 * (lineItem.getVarenummer())<br/>
	 * <p/>
	 * Was previously called: mkCreateServiceOrder
	 *
	 * @param modemId
	 *            selected service plan instance (key is modemId)
	 * @param lineItem
	 *            - data from casper
	 * @return smpOrder - en Order(), som allerede har kunde opret/opdater
	 *         udfyldt.
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when <br/>
	 *             1) The customer does not exist<br/>
	 */
	public Order createBB(ModemId modemId, AbonData lineItem) throws BusinessException {
		ensureAcct();

		InetAccess inetAccess = getModel().alloc().InetAccess(modemId);
		inetAccess.setModemId(modemId);
		inetAccess.sik.setValue(lineItem.getSik());
		inetAccess.rate_codes.setValue(lineItem.getRateCodes());
		inetAccess.setModemActivationCode(lineItem.getModemActivationCode());
		if (lineItem.getVrf() != null) {
			inetAccess.vrf.setValue(lineItem.getVrf());
		}

		if (inetAccess.internet_access_has_emta_cm.isEmpty()) {
			HsdAccess hsdAccess = getModel().find().HsdAccess(modemId);
			if (hsdAccess != null) {
				logger.info("Standard CPE HsdAccess was added for customer: " + getAcct() + ", for modemId:" + modemId);
				inetAccess.internet_access_has_emta_cm.add(hsdAccess);
			}
		}

		if (lineItem.isUsingStdCpe()) {
			//this was moved into ASU Cse
//			DeviceControl deviceControl = getModel().alloc().DeviceControl(modemId);
//			StdCpe stdCpe = getModel().alloc().StdCpe(modemId);

			if (lineItem.getStaticIpProductCode() != null) {
				SMPStaticIP smpStaticIP = getModel().alloc().SMPStaticIP(modemId);
				smpStaticIP.staticip_product_code.setValue(lineItem.getStaticIpProductCode());
				//this was moved into ASU Cse
//				if (smpStaticIP.static_ip_has_std_cpe.isEmpty()) {
//					smpStaticIP.static_ip_has_std_cpe.add(stdCpe);
//				}
			}
		}

		if (lineItem.getEmailServerUnblockProductCode() != null) {
			inetAccess.email_server_unblock_product_code.setValue(lineItem.getEmailServerUnblockProductCode());
		}
		if (lineItem.getWifiServiceProductCode() != null) {

			inetAccess.ss_id.setValue(InetAccess.generateSsid());
			inetAccess.psk.setValue(InetAccess.generatePsk());
			inetAccess.gw_channel_id.setValue("0");
		}
		return getModel().getOrder();
	}

	public Order updateBB(ModemId modemId, AbonData lineItem) throws BusinessException {
		ensureAcct();

		InetAccess inetAccess = getModel().alloc().InetAccess(modemId);
		inetAccess.sik.setValue(lineItem.getSik());
		if (lineItem.getRateCodes() != null) {
			inetAccess.rate_codes.setValue(lineItem.getRateCodes());
		}
		if (lineItem.getVrf() != null) {
			inetAccess.vrf.setValue(lineItem.getVrf());
		}

		inetAccess.setModemActivationCode(lineItem.getModemActivationCode());

		if (inetAccess.internet_access_has_emta_cm.isEmpty()) {
			HsdAccess hsdAccess = getModel().find().HsdAccess(modemId);
			if (hsdAccess != null) {
				logger.warn("Standard CPE HsdAccess was added for customer: " + getAcct() + ", for modemId:" + modemId);
				inetAccess.internet_access_has_emta_cm.add(hsdAccess);
			}
		}

		if (lineItem.isUsingStdCpe()) { // not make standard cpe or other
										// additional cpe's for M5 customers
			StdCpe stdCpe = getModel().find().StdCpe(modemId);
			if (stdCpe != null) {
				if (stdCpe.getServicePlanState() == ProvisionStateEnum.COURTESY_BLOCK) {
					stdCpe.sendAction(Action.SUSPEND);
				}
			}
			if (lineItem.getStaticIpProductCode() != null) {
				SMPStaticIP smpStaticIP = getModel().alloc().SMPStaticIP(modemId);
				smpStaticIP.staticip_product_code.setValue(lineItem.getStaticIpProductCode());
				if (smpStaticIP.static_ip_has_std_cpe.isEmpty()) {
					smpStaticIP.static_ip_has_std_cpe.add(stdCpe);
				}
			}
		}

		if (lineItem.getEmailServerUnblockProductCode() != null) {
			inetAccess.email_server_unblock_product_code.setValue(lineItem.getEmailServerUnblockProductCode());
		}
		if (lineItem.getWifiServiceProductCode() != null) {
			boolean exists = inetAccess.wifi_security_disabled.getValue().equals("false");
			if (!exists) { // this means it is created now, then generate
							// and fill in values
				inetAccess.ss_id.setValue(InetAccess.generateSsid());
				inetAccess.psk.setValue(InetAccess.generatePsk());
				inetAccess.gw_channel_id.setValue("0");
			}
		}
		if (lineItem.getAddnCPEProductCode() != null) {
			AddnCpe addnCpe = getModel().find().theAddnCpe(modemId);
			if (addnCpe == null) {
				addnCpe = getModel().add().AddnCpe(modemId);
				addnCpe.cpe_product_code.setValue(lineItem.getAddnCPEProductCode());
				addnCpe.cpe_service_id.setValue(addnCpe.getExternalKey());
			} else {
				addnCpe.cpe_product_code.setValue(lineItem.getAddnCPEProductCode());
			}
		}

		if (lineItem.getEmailServerUnblockProductCode() == null) {
			inetAccess.email_server_unblock_product_code.clearValue();
		}

		if (lineItem.getWifiServiceProductCode() == null) {
			inetAccess.wifi_security_disabled.setValue("true");
			inetAccess.ss_id.clearValue();
			inetAccess.psk.clearValue();
			inetAccess.psk_5g.clearValue();
			inetAccess.gw_channel_id.clearValue();
			inetAccess.ss_id_5g.clearValue();
			inetAccess.gw_channel_id_5g.clearValue();
		}

		if (lineItem.getStaticIpProductCode() == null) {
			SMPStaticIP smpStaticIP = getModel().find().SMPStaticIP(modemId);
			if (smpStaticIP != null) {
				smpStaticIP.delete();
			}
		}

		if (lineItem.getAddnCPEProductCode() == null) {
			AddnCpe addnCpe = getModel().find().theAddnCpe(modemId);
			if (addnCpe != null) {
				addnCpe.delete();
			}
		}

		return getModel().getOrder();
	}

	/**
	 * Inner class that holds the contract between cable broad band - seen from
	 * Casper/ Stallone and Sigma
	 */
	public static class AbonData {
		private String sik;

		public String getSik() {
			return sik;
		}

		public void setSik(String sik) {
			this.sik = sik;
		}

		private String rateCodes;
		private String staticIpProductCode;
		private String emailServerUnblockProductCode;
		private String wifiServiceProductCode;
		private String addnCPEProductCode;
		private String modemActivationCode;

		/**
		 * @param rateCodes
		 *            sample value: 4567 / silver
		 */
		public void setRateCodes(String rateCodes) {
			this.rateCodes = rateCodes;
		}

		public String getRateCodes() {
			return rateCodes;
		}

		/**
		 * Assign this field if the customer has static IP
		 *
		 * @param staticIpProductCode
		 *            sample value: "silver"
		 */
		public void setStaticIpProductCode(String staticIpProductCode) {
			this.staticIpProductCode = staticIpProductCode;
		}

		public String getStaticIpProductCode() {
			return staticIpProductCode;
		}

		/**
		 * @param emailServerUnblockProductCode
		 *            sample value "ABCD" in the cases from Sigma
		 */
		public void setEmailServerUnblockProductCode(String emailServerUnblockProductCode) {
			this.emailServerUnblockProductCode = emailServerUnblockProductCode;
		}

		public String getEmailServerUnblockProductCode() {
			return emailServerUnblockProductCode;
		}

		/**
		 * @param wifiServiceProductCode
		 *            field wifi_service_product_code sample value: 2948203984
		 */
		public void setWifiServiceProductCode(String wifiServiceProductCode) {
			this.wifiServiceProductCode = wifiServiceProductCode;
		}

		public String getWifiServiceProductCode() {
			return wifiServiceProductCode;
		}

		public String getAddnCPEProductCode() {
			return addnCPEProductCode;
		}

		public void setAddnCPEProductCode(String addnCPEProductCode) {
			this.addnCPEProductCode = addnCPEProductCode;
		}

		public String getModemActivationCode() {
			return modemActivationCode;
		}

		public void setModemActivationCode(String modemActivationCode) {
			this.modemActivationCode = modemActivationCode;
		}

		private String vrf;

		public String getVrf() {
			return vrf;
		}

		public void setVrf(String vrf) {
			this.vrf = vrf;
		}

		private boolean usingStdCpe = true;

		public boolean isUsingStdCpe() {
			return usingStdCpe;
		}

		public void setUsingStdCpe(boolean usingStdCpe) {
			this.usingStdCpe = usingStdCpe;
		}
	}

	/**
	 * Delete function
	 *
	 * @param modemId
	 *            selected service plan instance (key is modemId)
	 * @return true if anything was marked for delete, false if nothing marked
	 *         for delete.<br/>
	 *         Hereby the client can decide if anything needs to be send to
	 *         Sigma
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when <br/>
	 *             1) The customer does not exist<br/>
	 */
	public boolean deleteBB(ModemId modemId) throws BusinessException {
		ensureAcct();

		VoiceService voiceService = this.getModel().find().VoiceService(modemId);
		VoiceCase vc = new VoiceCase(this.getModel(), this.getService());
		boolean r1 = voiceService != null ? vc.deleteVoice(voiceService.getSik()) : false;
		boolean r2 = buildOrderFromAction(modemId, Action.DELETE);
		return r2 || r1;
	}

	/**
	 * Assign BB CPE to be suspended
	 *
	 * @param modemId
	 *            id to BB
	 * @param reason
	 *            why suspend
	 * @return resulting state - calculated as bss-adapter expects it to become
	 *         after order is (as expected) shipped to SMP
	 * @throws BusinessException
	 *             when customer does not exist ..
	 */
	public SuspendStatus suspendAbuse(ModemId modemId, SuspendReasonAbuse reason) throws BusinessException {
		ensureAcct();
		SuspendStatus spSt;
		StdCpe stdCpe = getModel().find().StdCpe(modemId);
		if (stdCpe != null) {
			stdCpe.sendAction(Action.SUSPEND);
			stdCpe.suspend_abuse.setValue(reason.name());
			spSt = new SuspendStatus(SuspendReasonAbuse.getEnum(stdCpe.suspend_abuse.getValue()),
					SuspendReasonBilling.getEnum(stdCpe.suspend_billing.getValue()));
			AddnCpe addnCpe = getModel().find().AddnCpe(modemId);
			if (addnCpe != null) {
				addnCpe.sendAction(Action.SUSPEND);
				addnCpe.suspend_abuse.setValue(reason.toString());
			}
		} else {
			spSt = new SuspendStatus(false);
		}
		return spSt;
	}

	/**
	 * Assign BB CPE to be suspended
	 *
	 * @param modemId
	 *            id to BB
	 * @param reason
	 *            why suspend
	 * @return resulting state - calculated as bss-adapter expects it to become
	 *         after order is (as expected) shipped to SMP
	 * @throws BusinessException
	 *             when customer does not exist ..
	 */
	public SuspendStatus suspendBilling(ModemId modemId, SuspendReasonBilling reason) throws BusinessException {
		ensureAcct();
		SuspendStatus spSt;
		StdCpe stdCpe = getModel().find().StdCpe(modemId);
		if (stdCpe != null) {
			stdCpe.sendAction(Action.SUSPEND);
			stdCpe.suspend_billing.setValue(reason.name());
			spSt = new SuspendStatus(SuspendReasonAbuse.getEnum(stdCpe.suspend_abuse.getValue()),
					SuspendReasonBilling.getEnum(stdCpe.suspend_billing.getValue()));
			AddnCpe addnCpe = getModel().find().AddnCpe(modemId);
			if (addnCpe != null) {
				addnCpe.sendAction(Action.SUSPEND);
				addnCpe.suspend_billing.setValue(reason.toString());
			}
		} else {
			spSt = new SuspendStatus(false);
		}
		return spSt;
	}

	/**
	 * Resume BB seen from service / abuse handling <br/>
	 * The suspend abuse will always be set to blank - or better called removed <br/>
	 * The subservices - on the other hand will be RESUMED or SUSPENDED -
	 * dependent on billing status.
	 *
	 * @param modemId
	 *            selected service plan instance (key is modemId)
	 * @return resulting suspend status, might still be suspended
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when <br/>
	 *             1) The customer does not exist<br/>
	 */
	public SuspendStatus resumeAbuse(ModemId modemId) throws BusinessException {
		ensureAcct();
		SuspendStatus spSt;
		StdCpe stdCpe = getModel().find().StdCpe(modemId);
		if (stdCpe != null) {
			Action resultingAction;
			stdCpe.suspend_abuse.setValue(" ");
			if (stdCpe.suspend_billing.hasValue()) {
				resultingAction = Action.SUSPEND;
				spSt = new SuspendStatus(SuspendReasonAbuse.getEnum(stdCpe.suspend_abuse.getValue()),
						SuspendReasonBilling.getEnum(stdCpe.suspend_billing.getValue()));
			} else {
				resultingAction = Action.RESUME;
				spSt = new SuspendStatus(true);
			}
			stdCpe.sendAction(resultingAction);
			AddnCpe addnCpe = getModel().find().AddnCpe(modemId);
			if (addnCpe != null) {
				stdCpe.suspend_abuse.setValue(" ");
				addnCpe.sendAction(resultingAction);
			}
		} else {
			spSt = new SuspendStatus(false);
		}
		return spSt;
	}

	/**
	 * Resume BB seen from business provisioning <br/>
	 * The suspend billing will always be set to blank - or better called
	 * removed <br/>
	 * The subservices - on the other hand will be RESUMED or SUSPENDED -
	 * dependent on abuse status.
	 *
	 * @param modemId
	 *            identify BB plan
	 * @return resulting suspend status, might still be suspended
	 * @throws BusinessException
	 *             when account is missing
	 */
	public SuspendStatus resumeBilling(ModemId modemId) throws BusinessException {
		ensureAcct();
		SuspendStatus spSt;
		StdCpe stdCpe = getModel().find().StdCpe(modemId);
		if (stdCpe != null) {
			Action resultingAction;
			stdCpe.suspend_billing.setValue(" ");
			if (stdCpe.suspend_abuse.hasValue()) {
				resultingAction = Action.SUSPEND;
				spSt = new SuspendStatus(SuspendReasonAbuse.getEnum(stdCpe.suspend_abuse.getValue()),
						SuspendReasonBilling.getEnum(stdCpe.suspend_billing.getValue()));
			} else {
				resultingAction = Action.RESUME;
				spSt = new SuspendStatus(true);
			}
			stdCpe.sendAction(resultingAction);
			AddnCpe addnCpe = getModel().find().AddnCpe(modemId);
			if (addnCpe != null) {
				stdCpe.suspend_billing.setValue(" ");
				addnCpe.sendAction(resultingAction);
			}
		} else {
			spSt = new SuspendStatus(false);
		}
		return spSt;
	}

	/**
	 * SuspendStatus to CSAM2 function
	 *
	 * @param modemId
	 *            selected service plan instance (key is modemId) checks the
	 *            status of the stdCPE
	 */
	/*
	 * public boolean suspendStatus(ModemId modemId) throws BusinessException {
	 * ensureAcct(); logger.info("suspend reason0"); boolean status = false;
	 * StdCpe stdCpe = getModel().find().StdCpe(modemId); if (stdCpe != null) {
	 * if
	 * (stdCpe.getServicePlanState().compareTo(ProvisionStateEnum.COURTESY_BLOCK
	 * ) == 0) { status = true; logger.info("suspend reason1" + status); } }
	 * return status; }
	 */
	/**
	 * @param modemId
	 *            identifier for BB service plan
	 * @return status instance
	 * @throws BusinessException
	 *             on fundamental errors - like no account yet.
	 */
	public SuspendStatus getSuspendStatus(ModemId modemId) throws BusinessException {
		ensureAcct();
		StdCpe stdCpe = getModel().find().StdCpe(modemId);
		SuspendStatus spSt;
		if (stdCpe != null) {
			if (stdCpe.getServicePlanState() == ProvisionStateEnum.COURTESY_BLOCK) {
				spSt = new SuspendStatus(SuspendReasonAbuse.getEnum(stdCpe.suspend_abuse.getValue()),
						SuspendReasonBilling.getEnum(stdCpe.suspend_billing.getValue()));
			} else {
				spSt = new SuspendStatus(true);
			}
		} else {
			spSt = new SuspendStatus(false);
		}
		return spSt;
	}

	/**
	 * Constructs an order from action change
	 *
	 * @param modemId
	 *            selected service plan instance (key is modemId)
	 * @param action
	 *            the action to send to the subscription
	 * @return true if anything to do
	 */
	private boolean buildOrderFromAction(ModemId modemId, Action action) {
		boolean doAnything = false;
		{
			CableBBService service = getModel().find().CableBBService(modemId);
			if (service != null) {
				doAnything = true;

				/**
				 * It was proven from tests that delete on top level works.<br/>
				 * But suspend/resume must be performed at each child-service
				 * <p>
				 * Tests shows that marking elements for something the element
				 * already is results in no order line in Sigma. This might be
				 * use full when sending commands to Sigma - so sending too much
				 * does not really matter in Sigma !!
				 * </p>
				 *
				 */
				if (action == Action.DELETE) {
					service.sendAction(action);
				} else {
					service.cascadeSendAction(action);
				}
			}
		}
		{
			MTAService cpe = getModel().find().MTAService(modemId);
			if (cpe != null) {
				doAnything = true;
				if (action == Action.DELETE) {
					cpe.sendAction(action);
				} else {
					cpe.cascadeSendAction(action);
				}
			}
		}
		return doAnything;
	}

	/**
	 * Update the customer's WiFi settings such as gw.channel.id, psk, ss_id -
	 * See PCR13
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
	 * @return model instance
	 */
	public InetAccess updateSMPWiFi(ModemId modemId, String gw_ch_id, String psk, String ss_id, String gw_ch_5g) {
		InetAccess inetAccess = getModel().find().InetAccess(modemId);
		if (inetAccess != null && inetAccess.wifi_security_disabled.getValue().equals("false")) {
			if (inetAccess != null) {
				logger.debug("gw_ch_id: " + gw_ch_id);
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

	public InetAccess updateSMPWiFi(ModemId modemId, String gw_ch_id, String psk, String ss_id, String gw_ch_5g, String psk_5g,
			String ss_id_5g) {
		InetAccess inetAccess = getModel().find().InetAccess(modemId);
		if (inetAccess != null && inetAccess.wifi_security_disabled.getValue().equals("false")) {
			if (gw_ch_id != null) {
				inetAccess.gw_channel_id.setValue(gw_ch_id);
			}
			if (psk != null) {
				inetAccess.psk.setValue(psk);
			}
			if (psk_5g != null) {
				inetAccess.psk_5g.setValue(psk_5g);
			}
			if (ss_id != null) {
				inetAccess.ss_id.setValue(ss_id);
			}
			if (ss_id_5g != null) {
				inetAccess.ss_id_5g.setValue(ss_id_5g);
			}
			if (gw_ch_5g != null) {
				inetAccess.gw_channel_id_5g.setValue(gw_ch_5g);
			}
		}
		return inetAccess;
	}

}
